package com.m4rc310.ml.ui.custom.databinding;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class TextObservableValue extends MAbstractObservableValue<String> implements Listener {

	private final Text text;

	private String oldValue;

	public TextObservableValue(Text text) {
		this.text = text;
		if(!text.isDisposed()) {
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

	private void setCCToText(String stext) {
		if (!text.isDisposed()) {
			try {
				text.setText(stext);
			} catch (Exception e) {
				text.setText("");
			}
		}
	}

	private String textToCC() {
		if (!text.isDisposed()) {
			return text.getText();
		}
		return "";
	}

//	@Override
//	public void handleEvent(Event event) {
//
//	}

	public void keyReleased(KeyEvent e) {
		Text widget = (Text) e.widget;
		String newValue = widget.getText();
		fireEvent(new ValueChangeEvent<>(this, Diffs.createValueDiff(oldValue, newValue)));
		oldValue = newValue;
	}

	
	@Override
	public void handleEvent(Event event) {
		try {
			Text widget = (Text) event.widget;
			String newValue = widget.getText();
			fireValueChange(Diffs.createValueDiff(oldValue, newValue));
//			fireEvent(new ValueChangeEvent<>(this, Diffs.createValueDiff(oldValue, newValue)));
//			super..super.fireChange();
			oldValue = newValue;

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
