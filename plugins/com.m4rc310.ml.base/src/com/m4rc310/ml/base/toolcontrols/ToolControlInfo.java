package com.m4rc310.ml.base.toolcontrols;

import javax.annotation.PostConstruct;

import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.m4rc310.rcp.ui.utils.CronUtils;
import com.m4rc310.rcp.ui.utils.PartControl;

public class ToolControlInfo {

	@PostConstruct
	public void createGui(Composite parent_, PartControl pc, CronUtils cronUtils, UISynchronize sync) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(1, false));
		Label labelAtencao = pc.getLabel(parent, "Atenção!");
		
		cronUtils.cron("2/1 * * * * *", new Runnable() {
			int i = 0;

			@Override
			public void run() {
				sync.asyncExec(() -> {
					boolean par = i++ % 2 == 0;
					if (!labelAtencao.isDisposed()) {
						labelAtencao.setVisible(par);
					}
				});
			}
		});
	}
}