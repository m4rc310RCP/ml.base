package reports.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;

import com.jasperassistant.designer.viewer.IReportViewer;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRRewindableDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

//@Creatable
//@Singleton
public class R {

	static {

	}

//	private final String THIS_PLUGIN_ID = "com.m4rc310.rcp.reports";
//	private final String KEY_INFO_REPORTS = "key.info.reports";
	private static final String REPORT_PATH = "reports";

	public static final String LOAD_JASPER_INFO = "load_jasper_info";

//	@Inject
//	@Preference(nodePath = THIS_PLUGIN_ID)
//	IEclipsePreferences prefs;

	@Inject
	@Optional
	Shell sheel;

//	@Inject EventBroker eventBroker;

	private static R get() {
//		Bundle bundler = FrameworkUtil.getBundle(Activator.class);
//		BundleContext btx = bundler.getBundleContext();

		IEclipseContext context = EclipseContextFactory.create();
		return ContextInjectionFactory.make(R.class, context);
	}

	public static JasperReport getReport(String pluginID, String name) {
		return get().getReport_(pluginID, name);
	}
	
	public static JasperReport getReport(String name) {
				int i = name.lastIndexOf(".");
		String sname = name.substring(i);
		String pluginId = name.replace(sname, "");
		
		sname = sname.replace(".", "");
		
		return getReport(pluginId, sname);
	}
	
	public static JRRewindableDataSource getJRBeanDataSourceObject(Object value) {
		try {
			return new JRBeanCollectionDataSource(Arrays.asList(value));
		} catch (Exception e) {
			return new JREmptyDataSource();
		}
	}

	public static JRRewindableDataSource getJRBeanDataSource(Collection<?> list) {
		try {
			return new JRBeanCollectionDataSource(list);
		} catch (Exception e) {
			return new JREmptyDataSource();
		}
	}

	public static void compileReports(String pluginID) {
		compileReports(pluginID, REPORT_PATH);
	}
	

	public static void compileReports(String pluginID, String dirName) {
		get().compileReports_(pluginID, dirName);
	}

	private void compileReports_(String pluginID, String dirName) {
		try {
			URL bundle = Platform.getBundle(pluginID).getResource(dirName);
			
			if(bundle == null) {
				return;
			}
			
			String path = FileLocator.toFileURL(bundle).getFile();
			
			File directory = new File(path, "compiled");
			directory.mkdir();

			List<File> list = new ArrayList<>();
			listf(path, list);

			for (File file : list) {
				String md5 = getHashMD5(file);

				String sdest = "%s/%s_&_%s.jasper";
				sdest = String.format(sdest, directory.getAbsolutePath(), file.getName().replace(".jrxml", ""), md5);

				if(compile(directory, sdest)) {
					
					System.out.print(String.format("Compilando o relatório %s... ", file.getName()));
					
					removeOldVersionOfReport(directory, file.getName().replace(".jrxml", ""));
					
					JasperDesign design = JRXmlLoader.load(file);
					JasperCompileManager.compileReportToFile(design, sdest);
					
					System.err.println("OK");
				}
			}

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
	
	private void removeOldVersionOfReport(File directory, String name) {
		for (File f : directory.listFiles()) {
			String path = f.getName();
			
			String regex = String.format("%s_&_\\w.*", name);
			
			if(path.matches(regex)) {
				f.delete();
			}
		}
	}

	private boolean compile(File directory, String sdest) {
		for (File f : directory.listFiles()) {
			if (sdest.equals(f.getAbsolutePath())) {
				return false;
			}
		}
		return true;
	}

	public static void listf(String directoryName, List<File> files) {
		File directory = new File(directoryName);
		File[] fList = directory.listFiles();
		for (File file : fList) {

			if (file.isDirectory()) {
				listf(file.getAbsolutePath(), files);
			} else if (file.getAbsolutePath().endsWith(".jasper")) {
			} else if (file.getAbsolutePath().endsWith(".jrxml")) {
				files.add(file);
			}
		}
	}

	public static void loadReport(IReportViewer view, String name, Map<String, Object> params, List<?> values) {
		JasperReport report = getReport(name);
		JasperPrint print = getJasperPrint(report, params, values);
		view.setDocument(print);
	}

	public static JasperPrint getJasperPrint(JasperReport report, Map<String, Object> params, List<?> values) {
		try {
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, params,
					new JRBeanCollectionDataSource(values));
			return jasperPrint;
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}

	private JasperReport getReport_(String pluginId, String reportName) {
		try {
			URL bundle = Platform.getBundle(pluginId).getResource("sreports");
			String path = FileLocator.toFileURL(bundle).getFile();
			
			File directory = new File(path, "compiled");
			directory.mkdir();
			
			for (File f : directory.listFiles()) {
				String sname = f.getName();
				String regex = String.format("%s_&_", reportName);
				if(sname.startsWith(regex)) {
					return (JasperReport) JRLoader.loadObject(f);
				}
			}
			
			throw new Exception("Report not found: " + reportName);
			
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public static String getHashMD5(File file) {
        try (InputStream is = new FileInputStream(file)) {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //byte[] buffer = new byte[80192];
            byte[] buffer = new byte[(int)file.length()];
            int read;
            while ((read = is.read(buffer)) > 0) {
                digest.update(buffer, 0, read);
            }
            
            byte[] md5sum = digest.digest();
            BigInteger bi = new BigInteger(1, md5sum);
            String out = bi.toString(16);
            return out;
        } catch (Exception ex) {
            return null;
        }

    }
	
	
//	private JasperReport getReport_(String name) {
//		
//		throw new UnsupportedOperationException("to-do");
//	}

}
