package com.m4rc310.ml.statusbar.toolcontrols;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.ui.parts.PartControl;

public class ToolcontrolStatusLeft {
	
	@Inject
	UISynchronize sync;
	
	
	@PostConstruct
	public void createGui(Composite c, PartControl pc, ActionStatusBar action) {
		
		Composite parent = pc.getComposite(c);
		GridData gd = new GridData();
		gd.widthHint=250;
		parent.setLayout(new GridLayout(4,false));
		
		pc.clearMargins(parent);
		
		Label labelSP = pc.getLabel(parent, "");
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		gd.widthHint=4;
		labelSP.setLayoutData(gd);

		Label labelIcon = pc.getLabel(parent, "");
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		gd.widthHint=16;
		gd.heightHint=16;
		labelIcon.setLayoutData(gd);
		
		gd = new GridData();
		gd.widthHint=5;
		
		Label labelSP2 = pc.getLabel(parent, "");
		labelSP2.setLayoutData(gd);
		
		Label labelText = pc.getLabel(parent, "");
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		gd.widthHint=380;
		labelText.setLayoutData(gd);
		
		parent.addDisposeListener(e->{
			action.removeListeners(ToolcontrolStatusLeft.class);
		});
		
		
		
		action.addListener(this, ActionStatusBar.LEFT_MESSAGE, e->{
			sync.asyncExec(()->{
				Image icon = e.getValue(0, Image.class);
				String text = e.getValue(1, String.class);
				if(!labelIcon.isDisposed() && !labelText.isDisposed()) {
					labelIcon.setImage(icon);
					labelText.setText(text);
					parent.layout();
				}
			});
		});
		
		
	}
}