package com.m4rc310.ml.statusbar.toolcontrols;

import java.util.Date;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.ui.DateUtils;
import com.m4rc310.ml.ui.hardware.info.E4ApplicationInfo;
import com.m4rc310.ml.ui.parts.PartControl;

public class ToolcontrolWatch {

	@Inject
	UISynchronize sync;

	@PostConstruct
	public void createGui(Composite parent_, PartControl pc, ActionStatusBar action, E4ApplicationInfo info) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(2, false));
		pc.clearMargins(parent);

//		parent.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		Label icon = pc.getIcon(parent, "com.m4rc310.ml.statusbar", "icons/clock.png");
		GridData gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
		gd.widthHint = 18;
		gd.heightHint = 16;

		icon.setLayoutData(gd);

		Label label = pc.getLabel(parent, "");
		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);

		String mask = "MMM, 88/88/8888 88:88 ";
		Dimension size = pc.getStringSize(mask, label.getFont());
		gd.widthHint = size.width;

		label.setLayoutData(gd);

		parent.addDisposeListener((e) -> {
			action.removeListeners(ToolcontrolWatch.this);
		});

		addListener(action, label);
		updateWatch(label, new Date());
	}

	private void addListener(ActionStatusBar action, Label label) {
		action.addListener(this, ActionStatusBar.SEND_DATE_TIME, e -> {
			Date date = e.getValue(Date.class);
			updateWatch(label, date);
		});
	}

	private void updateWatch(Label label, Date date) {
		sync.asyncExec(() -> {
			if (!label.isDisposed()) {
				label.setText(DateUtils.dateToString(date, "EEE, dd/MM/yyyy  HH:mm"));
				label.getParent().layout();
			}
		});

	}

}