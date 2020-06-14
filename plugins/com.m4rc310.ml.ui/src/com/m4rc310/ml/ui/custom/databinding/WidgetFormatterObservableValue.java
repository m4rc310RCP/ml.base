package com.m4rc310.ml.ui.custom.databinding;

import java.lang.reflect.Method;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Widget;

public class WidgetFormatterObservableValue<T> extends AbstractObservableValue<T>{
	
	
	private final Widget widget;
	private final MFormatter<T> formatter;
	private T value;

	Listener listener = event ->{
		T newValue = value;
		fireValueChange(Diffs.createValueDiff(value, newValue));
	};

	public WidgetFormatterObservableValue(Widget widget, MFormatter<T> formatter) {
		this.widget = widget;
		this.formatter = formatter;
		this.widget.addListener(SWT.Modify, listener);
	}

	@Override
	public Object getValueType() {
		if(this.value != null) {
			return this.value.getClass();
		}
		return null;
	}

	@Override
	protected T doGetValue() {
		return value;
	}
	
	@Override
	protected void doSetValue(T value) {
		this.value = value;
		setValueAndFormat(value);
	}
	
	private void setValueAndFormat(T value) {
		if(!widget.isDisposed()) {
			
			String svalue = formatter.toString(value);
			try {
				Method method = widget.getClass().getMethod("setText", String.class);
				if(method!=null) {
					method.setAccessible(true);
					method.invoke(widget, svalue);
				}
			} catch (Exception e) {
			}
		}
	}
	
	
//	public interface MFormatter<T>{
//		String toString(T value);
//	}
	
	@Override
	public synchronized void dispose() {
		this.widget.removeListener(SWT.Modify, listener);
		super.dispose();
	}

}
