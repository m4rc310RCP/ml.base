package com.m4rc310.ml.base.toolcontrols;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.m4rc310.ml.base.actions.ActionStatus;
import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.rcp.ui.utils.PartControl;

public class ToolcontrolStatus {

	@Inject
	@Translation
	M m;

	@Inject
	UISynchronize sync;

	@PostConstruct
	public void createGui(Composite parent_, PartControl pc, ActionStatus action) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout());
		Label label = pc.getLabel(parent, m.textStatus);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 350;

		label.setLayoutData(gd);

		action.addListener(this, ActionStatus.TEXT_STATUS, e -> {
			sync.asyncExec(() -> {
				label.setText(e.getValue(String.class));
				label.getParent().layout();
			});
		});
	}
}