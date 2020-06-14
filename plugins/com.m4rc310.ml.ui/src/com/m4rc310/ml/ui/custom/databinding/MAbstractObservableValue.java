package com.m4rc310.ml.ui.custom.databinding;

import org.eclipse.core.databinding.observable.ChangeEvent;
import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.databinding.observable.value.ValueChangeEvent;
import org.eclipse.core.databinding.observable.value.ValueDiff;

public abstract class MAbstractObservableValue<T> extends AbstractObservableValue<T> implements IObservableValue<T> {
	public MAbstractObservableValue() {
		this(Realm.getDefault());
	}

	/**
	 * @param realm the realm to use; not <code>null</code>
	 */
	public MAbstractObservableValue(Realm realm) {
		super(realm);
	}

	protected void fireValueChange(ValueDiff<T> diff) {
		// fire general change event first
		fireEvent(new ValueChangeEvent<>(this, diff));
		fireChange();
	}
	
	protected void fireChange() {
		checkRealm();
		fireEvent(new ChangeEvent(this));
	}

}
