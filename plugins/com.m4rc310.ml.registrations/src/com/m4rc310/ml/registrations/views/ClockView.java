package com.m4rc310.ml.registrations.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class ClockView extends ViewPart {
	

	@Override
	public void createPartControl(Composite parent) {
		final Canvas clock = new Canvas(parent, SWT.NONE);
		clock.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawArc(e.x, e.y, e.width - 1, e.height - 1, 0, 360);
			}
		});
	}

	@Override
	public void setFocus() {

	}

}
