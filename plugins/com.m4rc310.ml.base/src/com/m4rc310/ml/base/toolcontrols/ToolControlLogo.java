package com.m4rc310.ml.base.toolcontrols;

import java.io.File;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.internal.util.BundleUtility;

import com.m4rc310.rcp.ui.utils.PartControl;

@SuppressWarnings("restriction")
public class ToolControlLogo {

	@Inject
	IStylingEngine style;

	
	public void createGui_(Composite parent_, PartControl pc, Shell shell) {
		try {

			String path = "/fonts/Arthure.ttf";

			URL pathUrl = BundleUtility.find("com.m4rc310.ml.base", path);
			
			System.out.println(new File(pathUrl.toExternalForm()).exists());
			
			System.out.println(pathUrl.toExternalForm());
			System.out.println(pathUrl.toString());
			System.out.println(pathUrl.getPath());

//			boolean isLoaded = Display.getCurrent().loadFont(pathUrl.toExternalForm());

			MessageDialog.openInformation(shell, "", "" + new File(pathUrl.toExternalForm()).exists());
			
			

//			Bundle bundle = Activator.getDefault().getBundle();
//			Path path = new Path("fonts/helveticaNeueBold_iOS7.ttf");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	@PostConstruct
	public void createGui(Composite parent_, PartControl pc, Shell shell) {

		try {

			Composite parent = pc.getComposite(parent_);
			parent.setLayout(new GridLayout(2, false));

			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			parent.setLayoutData(gd);

			gd = new GridData(GridData.FILL_HORIZONTAL);
			pc.getLabel(parent, "").setLayoutData(gd);
			
			pc.loadFont("");

//			boolean isload = pc.loadFont("com.m4rc310.ml.base", "fonts/Oh Chewy.ttf");

			Label labelVenus = pc.getLabel(parent, "Vênus");

//			Font font = new Font(shell.getDisplay(), "Oh Chewy", 50, SWT.NORMAL);

//			font = JFaceResources.getFo

//			labelVenus.setFont(font);
			style.setClassname(labelVenus, "MLogo");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}