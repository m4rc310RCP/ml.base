package com.m4rc310.ml.base.actions;

import java.text.MessageFormat;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.m4rc310.rcp.ui.utils.MAction;

@Creatable
@Singleton
public class ActionStatus extends MAction {
	public static final String TEXT_STATUS = "text_status";
	
	
	public void status(String text, Object...args) {
		
		
		
		text = MessageFormat.format(text, args);
		fireInCache(TEXT_STATUS, text);
	}
}
