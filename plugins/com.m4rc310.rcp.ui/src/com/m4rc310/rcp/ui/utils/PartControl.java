package com.m4rc310.rcp.ui.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.core.databinding.DataBindingContext;
//import org.eclipse.core.databinding.beans.typed.BeanProperties;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.WritableList;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.jface.databinding.swt.ISWTObservableValue;
//import org.eclipse.jface.databinding.swt.typed.WidgetProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.framework.Bundle;

import com.m4rc310.rcp.ui.Activator;

@Creatable
@Singleton
@SuppressWarnings({ "deprecation", "unchecked", "rawtypes" })
public class PartControl {

	@Inject
	MApplication application;
	@Inject
	EPartService partService;
	@Inject
	EModelService modelService;

	@Inject
	IEventBroker eventBroker;

	@Inject
	UISynchronize sync;

	@Inject
	Shell shell;

	@Inject
	Display display;

	private final Map<Object, String> mapReferences = new HashMap<Object, String>();

	private DataBindingContext ctx = new DataBindingContext();

	private WritableList writableList;
	private GC gc;

	static boolean IS_DEBUG = false;
	private static final String WEB_CONTENT_DEVELOPMENT_FOLDER = "/WebContent-dev"; //$NON-NLS-1$
	private static final String WEB_CONTENT_RELEASE_FOLDER = "/WebContent-rel"; //$NON-NLS-1$
	private static final String RESOURCE_PATH = "/ml/resources/"; //$NON-NLS-1$

	private static final String WEB_CONTENT_FOLDER = IS_DEBUG ? WEB_CONTENT_DEVELOPMENT_FOLDER
			: WEB_CONTENT_RELEASE_FOLDER;
	private static final String URL_SPACE = " "; //$NON-NLS-1$
	private static final String URL_SPACE_REPLACEMENT = "%20"; //$NON-NLS-1$
	private static final String URL_SQB_OPEN = "\\["; //$NON-NLS-1$
	private static final String URL_SQB_OPEN_REPLACEMENT = "%5B"; //$NON-NLS-1$
	private static final String URL_SQB_CLOSE = "\\]"; //$NON-NLS-1$
	private static final String URL_SQB_CLOSE_REPLACEMENT = "%5D"; //$NON-NLS-1$

	@Inject
	public PartControl() {

	}

	public void dispose() {
		ctx.dispose();
	}

	public boolean loadFont(String pluginId, String path) {
		try {

//			final File fontFile = getResourceFile("fonts/Arthure.ttf");

//			System.outs.println(fontFile);

//			URL bundle = Platform.getBundle(pluginId).getResource(path);
//			path = FileLocator.toFileURL(bundle).getFile();

//			Shell shell = new Shell();
//			MessageDialog.openInformation(shell, "", path);
//			MessageDialog.openInformation(shell, "", new File(path).exists() +"");

			return display.loadFont(new File(path).getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean loadFont(String path) {
		return loadFont("com.m4rc310.rcp.ui", path);
	}

	public void stackLayout(Composite parent) {
		parent.setLayout(new StackLayout());
	}

//	public ScrolledComposite getScrolledComposite(Composite parent) {
//
//		ScrolledComposite scroll = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//
//		return scroll;
//	}

	public Dimension getTextSize(String text, Font font) {
		setFont(font);
		return getTextSize(text);
	}

	public Dimension getStringSize(String string, Font font) {
		setFont(font);
		return getStringSize(string);
	}

	public Dimension getStringSize(String text) {
		return new Dimension(getGc().stringExtent(text));
	}

	public Dimension getTextSize(String text) {
		return new Dimension(getGc().textExtent(text));
	}

	private void setFont(Font font) {
		getGc().setFont(font);
	}

	public GC getGc() {
		if (this.gc == null) {
			this.gc = new GC(new Shell());
		}
		return gc;
	}

	public ImageData convertToSWT(BufferedImage bufferedImage) {
		if (bufferedImage.getColorModel() instanceof DirectColorModel) {
			DirectColorModel colorModel = (DirectColorModel) bufferedImage.getColorModel();
			PaletteData palette = new PaletteData(colorModel.getRedMask(), colorModel.getGreenMask(),
					colorModel.getBlueMask());
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[3];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					int pixel = palette.getPixel(new RGB(pixelArray[0], pixelArray[1], pixelArray[2]));
					data.setPixel(x, y, pixel);
				}
			}
			return data;
		} else if (bufferedImage.getColorModel() instanceof IndexColorModel) {
			IndexColorModel colorModel = (IndexColorModel) bufferedImage.getColorModel();
			int size = colorModel.getMapSize();
			byte[] reds = new byte[size];
			byte[] greens = new byte[size];
			byte[] blues = new byte[size];
			colorModel.getReds(reds);
			colorModel.getGreens(greens);
			colorModel.getBlues(blues);
			RGB[] rgbs = new RGB[size];
			for (int i = 0; i < rgbs.length; i++) {
				rgbs[i] = new RGB(reds[i] & 0xFF, greens[i] & 0xFF, blues[i] & 0xFF);
			}
			PaletteData palette = new PaletteData(rgbs);
			ImageData data = new ImageData(bufferedImage.getWidth(), bufferedImage.getHeight(),
					colorModel.getPixelSize(), palette);
			data.transparentPixel = colorModel.getTransparentPixel();
			WritableRaster raster = bufferedImage.getRaster();
			int[] pixelArray = new int[1];
			for (int y = 0; y < data.height; y++) {
				for (int x = 0; x < data.width; x++) {
					raster.getPixel(x, y, pixelArray);
					data.setPixel(x, y, pixelArray[0]);
				}
			}
			return data;
		}
		return null;
	}

	public Composite getStackComposite(Composite parent) {
		Composite stack = getComposite(parent);
		stack.setLayout(new StackLayout());
		return stack;
	}

	public void toTopControl(Control control) {
		try {
			Composite parent = control.getParent();
			Layout layout = parent.getLayout();
			if (layout instanceof StackLayout) {
				StackLayout sl = (StackLayout) layout;
				sl.topControl = control;
				parent.layout();
			}
		} catch (Exception e) {
		}
	}

	public void observeCheck(Widget widget, String field, Object bean) {
		ISWTObservableValue target = WidgetProperties.selection().observe(widget);
		IObservableValue<Object> model = BeanProperties.value(field).observe(bean);
		ctx.bindValue(target, model);
	}

	public void observeComboList(Widget widget, String field, Object bean) {
//		 ISWTObservableValue<String> target = WidgetProperties.comboSelection().observe(widget);
		ISWTObservableValue target = WidgetProperties.selection().observe(widget);
		IObservableValue<Object> model = BeanProperties.value(field).observe(bean);
		ctx.bindValue(target, model);
	}

	public void observeTextString(Widget textWidget, String field, Object bean) {
		ISWTObservableValue target = WidgetProperties.text(new int[] { SWT.Modify }).observe(textWidget);
		IObservableValue<Object> model = BeanProperties.value(field).observe(bean);
		ctx.bindValue(target, model);
	}

	public void registerViewerSupport(Class<?> type, StructuredViewer viewer, List<?> list, String... propertieNames) {
		this.writableList = new WritableList<>(list, type);
		ViewerSupport.bind(viewer, writableList, BeanProperties.values(propertieNames));
	}

	public String addNumberToLabel(Object ref, String text, int value) {
		if (mapReferences.containsKey(ref)) {
			return String.format("%s (%d)", mapReferences.get(ref), value);
		}

		mapReferences.put(ref, text);
		return String.format("%s (%d)", text, value);
	}

	public void enabled(boolean enabled, Object... controls) {
		for (Object c : controls) {
			if (c instanceof Control) {
				Control w = (Control) c;
				w.setEnabled(enabled);
			}

			if (c instanceof Viewer) {
				Viewer v = (Viewer) c;
				v.getControl().setEnabled(enabled);
			}
		}
	}

	public void visible(String partUri, boolean visible) {
		MPart part = partService.findPart(partUri);
		if (part == null) {
			part = partService.createPart(partUri);
		}

		for (String variable : part.getVariables()) {
			if (variable.contains("partStack:")) {
				variable = variable.replace("partStack:", "");

				MPartStack stack = modelService.findElements(application, variable, MPartStack.class, null).get(0);
				stack.setVisible(true);

				List<MStackElement> childres = stack.getChildren();
				childres.add(part);

				break;
			}
		}
		if (visible) {
			partService.showPart(part, PartState.ACTIVATE);
		} else {
			partService.hidePart(part);
		}
	}

	public void configureUpperCase(Text... texts) {
		for (Text text : texts) {
			text.addVerifyListener(new VerifyListener() {
				@Override
				public void verifyText(VerifyEvent e) {
					e.text = e.text.toUpperCase();
				}
			});
		}
	}

	public void configureLowerCase(Text... texts) {
		for (Text text : texts) {
			text.addVerifyListener(new VerifyListener() {
				@Override
				public void verifyText(VerifyEvent e) {
					e.text = e.text.toLowerCase();
				}
			});
		}
	}

	public void configureVerifyValues(String regex, Text... texts) {
		for (Text text : texts) {
			text.addVerifyListener(e -> {
				if (e.character == SWT.BS || e.character == SWT.DEL || e.text.isEmpty()) {
					return;
				}
				String s = e.text;
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(s);
				e.doit = matcher.matches();
			});
		}
	}

	public static File getResourceFile(final String fileName) throws IOException, URISyntaxException {

		final String bundleFileName = WEB_CONTENT_FOLDER + RESOURCE_PATH + fileName;
//		final String bundleFileName = fileName;

		final URL bundleUrl = Activator.getDefault().getBundle().getEntry(bundleFileName);

		if (bundleUrl == null) {
			System.out.println("File is not available: " + bundleFileName);

//			StatusUtil.log();//$NON-NLS-1$
			return null;
		}

		final File file = getEscapedBundleFile(bundleUrl);

		return file;
	}

	public static File getEscapedBundleFile(final URL bundleUrl) {

		File file = null;

		try {

			final URL fileUrl = FileLocator.toFileURL(bundleUrl);
			final String encodedFileUrl = encodeSpace(fileUrl.toExternalForm());

			final URI uri = new URI(encodedFileUrl);
			file = new File(uri);

		} catch (final Exception e) {
		}

		return file;
	}

	public static String encodeSpace(final String urlString) {

		String escaped;

		escaped = urlString.replaceAll(URL_SPACE, URL_SPACE_REPLACEMENT);
		escaped = escaped.replaceAll(URL_SQB_OPEN, URL_SQB_OPEN_REPLACEMENT);
		escaped = escaped.replaceAll(URL_SQB_CLOSE, URL_SQB_CLOSE_REPLACEMENT);

		return escaped;
	}

	public void configureNumericValues(Text... texts) {
		configureVerifyValues("[0-9,]", texts);
	}

	public CTabFolder createCTabFolder(Composite parent) {
		CTabFolder cTabFolder = new CTabFolder(parent, SWT.NONE);
		cTabFolder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));
		return cTabFolder;
	}

	public CTabItem createCTabItem(CTabFolder tabFolder, String title) {
		return createCTabItem(tabFolder, title, null);
	}

	public CTabItem createCTabItem(CTabFolder tabFolder, String title, Image icon) {
		CTabItem item = new CTabItem(tabFolder, SWT.NONE);
		item.setImage(icon);
		item.setText(title);
		return item;
	}

	public void margins(Composite composite, int width, int heigth, int vertical, int horizontal) {
		Layout layout = composite.getLayout();
		if (layout == null) {
			return;
		}

		if (layout instanceof GridLayout) {
			GridLayout gl = (GridLayout) layout;
			gl.verticalSpacing = vertical;
			gl.marginWidth = width;
			gl.marginHeight = heigth;
			gl.horizontalSpacing = horizontal;
		}
	}

	public void clearMargins(Composite... composites) {
		for (Composite composite : composites) {
			Layout layout = composite.getLayout();
			if (layout == null) {
				return;
			}

			if (layout instanceof GridLayout) {
				clearMargins((GridLayout) layout);
			}
		}

	}

	public void clearMargins(GridLayout gl) {
		gl.verticalSpacing = 0;
		gl.marginWidth = 0;
		gl.marginHeight = 0;
		gl.horizontalSpacing = 0;
	}

	public Image resize(Image image, int width, int height) {
		Image scaled = new Image(Display.getDefault(), width, height);
		GC gc = new GC(scaled);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);
		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
		gc.dispose();

		// Image data from scaled image and transparent pixel from original

		ImageData imageData = scaled.getImageData();

		imageData.transparentPixel = image.getImageData().transparentPixel;

		// Final scaled transparent image

		Image finalImage = new Image(Display.getDefault(), imageData);

		scaled.dispose();

		return finalImage;

//		Image scaled = new Image(Display.getDefault(), width, height);
//		GC gc = new GC(scaled);
//		gc.setAntialias(SWT.ON);
//		gc.setInterpolation(SWT.HIGH);
//		gc.drawImage(image, 0, 0, image.getBounds().width, image.getBounds().height, 0, 0, width, height);
//		gc.dispose();
//		image.dispose(); // don't forget about me!
//		return scaled;
	}

	public void show(String partUri, Object value) {
		show(partUri, null, value);
	}

	public void show(String partUri, String title, Object value) {
		show(partUri, partUri, title, value);
	}

	private final Map<String, MPart> partsMap = new HashMap<>();

	public void show(String partUri, String partId, String title, Object value) {

		sync.syncExec(() -> {

			try {

				MPart part;

				if (partsMap.containsKey(partId)) {
					part = partsMap.get(partId);
					part.setObject(value);

					for (String variable : part.getVariables()) {
						if (variable.contains("partStack:")) {
							variable = variable.replace("partStack:", "");
							MPartStack stack = modelService.findElements(application, variable, MPartStack.class, null)
									.get(0);
							stack.setVisible(true);
							List<MStackElement> childres = stack.getChildren();
							childres.add(part);
							break;
						}
					}

					partService.showPart(part, PartState.ACTIVATE);
					if (value != null) {
						eventBroker.send("update_part_report", part);
					}

					return;
				}

				part = partService.createPart(partUri);
				part.setElementId(partId);

				if (title != null) {
					part.setLabel(title);
				}

				part.setObject(value);

				for (String variable : part.getVariables()) {
					if (variable.contains("partStack:")) {
						variable = variable.replace("partStack:", "");
						MPartStack stack = modelService.findElements(application, variable, MPartStack.class, null)
								.get(0);
						stack.setVisible(true);
						List<MStackElement> childres = stack.getChildren();
						childres.add(part);
						break;
					}
				}

				partService.showPart(part, PartState.ACTIVATE);
				eventBroker.send("update_part_report", part);

				partsMap.put(partId, part);
			} catch (Exception e) {
				e.printStackTrace();
			}

		});

	}

	public Image getImage(String pluginId, String path) {
		try {
			return ResourceManager.getPluginImage(pluginId, path);
		} catch (Exception e) {
			return null;
		}
	}

	public ImageDescriptor createImageDescriptor(String pluginId, String path) {
		try {
			Bundle bundle = Platform.getBundle(pluginId);
			URL uri = bundle.getEntry(path);
			return ImageDescriptor.createFromURL(uri);
		} catch (Exception e) {
			return null;
		}

	}

	public void show(String partUri) {
		show(partUri, null);
	}

	public Composite getComposite(Composite parent, int style) {
		Composite ret = new Composite(parent, style);
		ret.setLayout(new GridLayout());
		clearMargins(ret);
		return ret;
	}

	public Group getGroup(Composite parent, int style) {
		Group ret = new Group(parent, style);
		return ret;
	}

	public Group getGroup(Composite parent) {
		return getGroup(parent, SWT.NONE);
	}

	public Composite getComposite(Composite parent) {
		return getComposite(parent, SWT.NONE);
	}

	public Button getButton(Composite parent, String text) {
		return getButton(parent, text, SWT.PUSH, null);
	}

	public Button getButton(Composite parent, String text, Listener listener) {
		return getButton(parent, text, SWT.PUSH, listener);
	}

	public Button getButton(Composite parent, String text, int style) {
		return getButton(parent, text, style, null);
	}

	public Button getButton(Composite parent, String text, int style, Listener listener) {
		Button ret = new Button(parent, style);
		ret.setText(text);
		if (listener != null) {
			ret.addListener(SWT.Selection, listener);
		}
		return ret;
	}

	public ComboViewer getComboViewer(Composite parent) {
		return getComboViewer(parent, SWT.DROP_DOWN);
	}

	public ComboViewer getComboViewer(Composite parent, int style) {
		return new ComboViewer(parent, style);
	}

	public Label getLabel(Composite parent, String text) {
		return getLabel(parent, text, SWT.NONE);
	}

	public Label getLabel(Composite parent, String text, int style) {
		Label ret = new Label(parent, style);
		ret.setText(text);
		return ret;
	}

	public Label getIcon(Composite parent, String plugin, String path) {
		Label ret = getLabel(parent, "");
		Image img = ResourceManager.getPluginImage(plugin, path);
		ret.setImage(img);
		return ret;
	}

	public Text getText(Composite parent, String text) {
		return getText(parent, text, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
	}

	public Text getText(Composite parent, String text, int style) {
		Text ret = new Text(parent, style);
		ret.setText(text);

		ret.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Text t = (Text) e.widget;
				t.selectAll();
				super.focusGained(e);
			}
		});

		return ret;
	}

	public void createColumn(TreeViewer viewer, String title, int width, int style, IBaseLabelProvider labelProvider) {
		TreeViewerColumn viewerColumn = new TreeViewerColumn(viewer, style);
		viewerColumn.getColumn().setWidth(width);
		viewerColumn.getColumn().setText(title);
		viewerColumn.setLabelProvider((CellLabelProvider) labelProvider);
	}

	public void createCollumn(TableViewer viewer, int style, String title, int width) {
		TableViewerColumn colFirstName = new TableViewerColumn(viewer, style);
		colFirstName.getColumn().setWidth(width);
		colFirstName.getColumn().setText(title);

	}

	public void createCollumn(TableViewer viewer, int style, String title, int width, IBaseLabelProvider provider) {
		TableViewerColumn colFirstName = new TableViewerColumn(viewer, style);
		colFirstName.getColumn().setWidth(width);
		colFirstName.getColumn().setText(title);
		colFirstName.setLabelProvider((CellLabelProvider) provider);
	}

	public void configureDateField(String format, Text... texts) {
//		configureVerifyValues("[0-9/]", texts);
		for (Text text : texts) {
			text.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent e) {
					Text t = (Text) e.widget;
					sync.asyncExec(() -> {
						try {
							String sdate = t.getText();

							if (sdate.isEmpty()) {
								return;
							}

							Date date = DateUtils.getDate(sdate);
							sdate = DateUtils.dateToString(date, format);
							t.setText(sdate);
							t.setData(date);
						} catch (Exception e2) {
							t.setFocus();
							t.selectAll();
						}
					});
				}
			});
		}
	}

	public GridData setGriData(Control control, int w) {
		GridData gd = new GridData();
		gd.widthHint = w;
		control.setLayoutData(gd);
		return gd;
	}

	public void addTextModifyListener(Text text, Listener listener) {
		text.addListener(SWT.Modify, listener);
	}

	public String toString(String pattern, Object... args) {
		return MessageFormat.format(pattern, args);
	}

	public void setDefaultButton(Button button) {
		Composite parent = button.getParent();
		parent.getShell().setDefaultButton(button);
	}

	public Color getColor(int color) {
		return Display.getCurrent().getSystemColor(color);
	}
	
	public ScrolledComposite getScrolledComposite(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		sc.setAlwaysShowScrollBars(true);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		return sc;
	}
	

	public void setContentToScrolled(Composite content) {
		Composite parent = content.getParent();
		if (parent instanceof ScrolledComposite) {
			ScrolledComposite scrolled = (ScrolledComposite) parent;
			scrolled.setContent(content);
			scrolled.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

	}

	

//	public Composite getCompositeScrolled(Composite parent) {
//		ScrolledComposite sc = getScrolledComposite(parent);
//		sc.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
//
//		Composite ret = getComposite(sc);
//		sc.setContent(ret);
//		sc.setExpandHorizontal(true);
//		sc.setExpandVertical(true);
//		sc.setMinSize(ret.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//
//		return ret;
//	}

	public void setGridDataWidthHint(Control control, int width) {
		GridData gd = new GridData();
		gd.widthHint = width;
		control.setLayoutData(gd);
	}

}
