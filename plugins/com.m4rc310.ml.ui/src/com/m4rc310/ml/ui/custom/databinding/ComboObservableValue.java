package com.m4rc310.ml.ui.custom.databinding;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;

public class ComboObservableValue extends MAbstractObservableValue<Object> implements ISelectionChangedListener {

	private final ComboViewer combo;

	private Object oldValue;

	public ComboObservableValue(ComboViewer combo) {
		this.combo = combo;
		if (!combo.getCombo().isDisposed()) {
			this.combo.addSelectionChangedListener(this);
		}
	}

	@Override
	public Object getValueType() {
		if (oldValue != null) {
			return oldValue.getClass();
		}
		return Object.class;
	}

	@Override
	protected Object doGetValue() {
		if (!combo.getCombo().isDisposed()) {
			StructuredSelection selection = (StructuredSelection) combo.getSelection();
			return selection.getFirstElement();
		}
		return null;
	}

	@Override
	protected void doSetValue(Object value) {
		if (!combo.getCombo().isDisposed()) {
			if (value != null) {
				StructuredSelection ss = new StructuredSelection(value);
				Display.getCurrent().asyncExec(() -> combo.setSelection(ss));
			} else {
				combo.setSelection(new StructuredSelection());
			}
		}
	}

	@Override
	public synchronized void dispose() {
		combo.removeSelectionChangedListener(this);
		super.dispose();
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		IStructuredSelection sts = (IStructuredSelection) event.getSelection();
		Object newValue = sts.getFirstElement();
		if (newValue != null) {
			if (!newValue.equals(oldValue)) {
				fireValueChange(Diffs.createValueDiff(oldValue, newValue));
				oldValue = newValue;
			}
		}
	}

}
