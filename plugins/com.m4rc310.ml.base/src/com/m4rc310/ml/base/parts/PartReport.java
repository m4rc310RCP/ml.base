
package com.m4rc310.ml.base.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.EMenuService;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jasperassistant.designer.viewer.IReportViewer;
import com.jasperassistant.designer.viewer.ViewerComposite;
import com.m4rc310.ml.base.actions.ActionML;
import com.m4rc310.ml.base.i18n.M;

import net.sf.jasperreports.engine.JasperPrint;

@SuppressWarnings("restriction")
public class PartReport {
	private ViewerComposite viewReportComposite;
	@Inject
	private MPart part;
	private JasperPrint print;

	@Inject
	UISynchronize sync;
	
	@Inject
	ActionML action;
	
	@Inject @Translation M m;

	@Inject
	public PartReport() {

	}

	boolean isloaded = false;
	
	@PostConstruct
	public void postConstruct(Composite parent_, EMenuService menu) {
		final Composite parent = new Composite(parent_, SWT.NONE);
		final GridLayout layout = new GridLayout();
		parent.setLayout(layout);

		this.viewReportComposite = new ViewerComposite(parent, SWT.NONE);
		viewReportComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));

		
		String label = part.getLabel();
		
		part.setLabel(m.labelLoading);
		
		
		action.addListener(this, ActionML.LOAD_REPORT_TEST, e->{
			JasperPrint print = e.getValue(JasperPrint.class);
			IReportViewer view = viewReportComposite.getReportViewer();
			view.setDocument(print);
			
			if(!isloaded) {
				viewReportComposite.getReportViewer().setZoomMode(IReportViewer.ZOOM_MODE_FIT_WIDTH);
				isloaded = true;
			}
			
			part.setLabel(label);
		});
		
	}
	
	@PreDestroy
	public void preDestroy() {
		action.removeListeners(this);
	}
	

//	@Inject
//	@Optional
	public void updatePart(@EventTopic("update_part_report") MPart part) {
		sync.asyncExec(() -> {
			if (this.part == part) {
				
				part.setLabel("Loading...");
				
				Object value = part.getObject();
				
				if (value instanceof JasperPrint) {
					this.print = (JasperPrint) part.getObject();
					IReportViewer view = viewReportComposite.getReportViewer();
					view.setDocument(print);
//					part.setLabel(print.getName());
					
				}
			}
		});

	}

}