/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m4rc310.rcp.ui.utils.streaming;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventObject;
import java.util.List;

/**
 *
 * @author Marcelo
 */
public class MEvent extends EventObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Object[] values;
	private String propertyName;

	
	public MEvent(Object source, String propertyName, Object... values) {
		super(source);
		this.propertyName = propertyName;
		this.values = values;
	}

	public static MEvent event(Object source, String propertyName, Object... values) {
		return new MEvent(source, propertyName, values);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public Object[] getValues() {
		return values;
	}

	public Object getValue() {
		return getValue(0);
	}

	public Object getValue(int index) {
		return values[index];
	}

	public <T extends Object> T getValue(Class<T> type) {
		return getValue(0, type);
	}

	public <T extends Object> T getValue(int index, Class<T> type) {
		@SuppressWarnings("unchecked")
		T ret = (T) values[index];
		return ret;
	}

	public <T> List<T> getListValue(int index, Class<? extends T> clazz) {
		Collection<?> c = getValue(0, List.class);
		List<T> r = new ArrayList<T>(c.size());
		for (Object o : c)
			r.add(clazz.cast(o));
		return r;
	}

	
	
	
	/**
	 * @param values the values to set
	 */
	public void setValues(Object... values) {
		this.values = values;
	}

	/**
	 * @param propertyName the propertyName to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	@Override
	public String toString() {
		String s = String.format("Property: %s, %d values, source: %s", propertyName, values.length, source);
		return s;
	}

}
