
package com.m4rc310.ml.ui.custom.databinding;

import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.ui.DateUtils;

public class DateTimeObservableValue extends MAbstractObservableValue<Date> {

	private final Text dateTime;
	private final String format;

	protected Date oldValue;

	Listener listener = event -> {
		try {
			Date newValue = dateTimeToDate();
			if (!newValue.equals(DateTimeObservableValue.this.oldValue)) {
				fireValueChange(Diffs.createValueDiff(DateTimeObservableValue.this.oldValue, newValue));
				DateTimeObservableValue.this.oldValue = newValue;
			}
		} catch (Exception e) {
			DateTimeObservableValue.this.oldValue = null;
		}
	};

//	FocusListener listener = new FocusAdapter() {
//		@Override
//		public void focusLost(FocusEvent event) {
//			Date newValue = dateTimeToDate();
//			try {
//				if (!newValue.equals(DateTimeObservableValue.this.oldValue)) {
//					fireValueChange(Diffs.createValueDiff(DateTimeObservableValue.this.oldValue,
//							newValue));
//					DateTimeObservableValue.this.oldValue = newValue;
//				}
//			} catch (Exception e) {
//				DateTimeObservableValue.this.oldValue = null;
//			}
//		}
//	};

	public DateTimeObservableValue(final Text dateTime, String format) {
		this.format = format;
		this.dateTime = dateTime;
		if (!dateTime.isDisposed()) {
			this.dateTime.addListener(SWT.Modify, this.listener);
		}
	}

	@Override
	protected Date doGetValue() {
		return dateTimeToDate();
	}

	@Override
	protected void doSetValue(final Date value) {
		dateToDateTime(value);
	}

	@Override
	public Object getValueType() {
		return Date.class;
	}

	private void dateToDateTime(final Date date) {
		if (!this.dateTime.isDisposed()) {
			this.dateTime.setText("");
			String sdate = DateUtils.dateToString(date, format);
			this.dateTime.setText(sdate);
		}
	}

	private Date dateTimeToDate() {
		Date result = null;
		if (!this.dateTime.isDisposed()) {
			try {
				result = DateUtils.getDate2(dateTime.getText());
			} catch (Exception e) {
			}
		}
		return result;
	}

	@Override
	public synchronized void dispose() {
//		this.dateTime.removeFocusListener(this.listener);
		this.dateTime.removeListener(SWT.Modify, this.listener);
		super.dispose();
	}

}
