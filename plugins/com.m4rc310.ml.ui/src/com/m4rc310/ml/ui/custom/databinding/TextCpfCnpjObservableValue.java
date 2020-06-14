package com.m4rc310.ml.ui.custom.databinding;

import org.brazilutils.br.cpfcnpj.CpfCnpj;
import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextCpfCnpjObservableValue extends AbstractObservableValue<String> implements Listener {

	private final Text text;
	private Color backgroundDefault;
	private Color backgroundError;

	private String oldValue;

	public TextCpfCnpjObservableValue(Text text) {
		this.text = text;
		if (!text.isDisposed()) {
			this.backgroundDefault = Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN);
			this.backgroundError = Display.getCurrent().getSystemColor(SWT.COLOR_RED);
			this.text.addListener(SWT.Modify, this);
		}
	}

	@Override
	public Object getValueType() {
		return String.class;
	}

	@Override
	protected String doGetValue() {
		return textToCC();
	}

	@Override
	protected void doSetValue(String scc) {
		setCCToText(scc);
	}

	@Override
	public synchronized void dispose() {
		this.text.removeListener(SWT.Modify, this);
		super.dispose();
	}

	private void setCCToText(String cc) {
		if (!text.isDisposed()) {
			try {
				text.setText(new CpfCnpj(cc).getCpfCnpj());
			} catch (Exception e) {
				text.setText("");
			}
		}
	}

	private String textToCC() {
		CpfCnpj cc = null;
		if (!text.isDisposed()) {
			cc = new CpfCnpj(text.getText());
		}
		return cc.getCpfCnpj();
	}

	@Override
	public void handleEvent(Event event) {
		String newValue = textToCC();
		try {
			if (newValue == null) {
				Display.getCurrent().asyncExec(() -> text.setBackground(backgroundError));
				return;
			}

			if (!newValue.equals(oldValue)) {
				CpfCnpj cc = new CpfCnpj(newValue);
				Color bg = cc.isValid() ? backgroundDefault : backgroundError;

				Display.getCurrent().asyncExec(() -> text.setForeground(bg));

				fireValueChange(Diffs.createValueDiff(oldValue, newValue));
				this.oldValue = newValue;
			}

		} catch (Exception e) {
		}

	}

}
