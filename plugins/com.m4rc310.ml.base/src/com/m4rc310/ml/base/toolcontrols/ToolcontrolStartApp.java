package com.m4rc310.ml.base.toolcontrols;

import javax.annotation.PostConstruct;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.m4rc310.rcp.ui.utils.PartControl;

public class ToolcontrolStartApp {
	
	@PostConstruct
	public void createGui(Composite parent_, PartControl pc) {
		Group parent = pc.getGroup(parent_);
		parent.setLayout(new GridLayout(1, false));
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		parent.setLayoutData(gd);
	}
}