package com.m4rc310.ml.statusbar.toolcontrols;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.ui.hardware.info.E4ApplicationInfo;
import com.m4rc310.ml.ui.parts.PartControl;

public class ToolcontrolVersion {

	@Inject
	UISynchronize sync;

	@PostConstruct
	public void createGui(Composite parent_, PartControl pc, ActionStatusBar action, E4ApplicationInfo info) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(2,false));
		
		pc.clearMargins(parent);
		
		String sinfo = String.format("%s - %s", info.getAppName(), info.getAppVersion());
		Label label = pc.getLabel(parent, sinfo);

		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		label.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint = 5;
		
		Label sp = pc.getLabel(parent, "");
		sp.setLayoutData(gd);
		
		
	}
}