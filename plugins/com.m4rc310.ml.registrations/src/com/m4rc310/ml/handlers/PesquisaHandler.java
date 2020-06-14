 
package com.m4rc310.ml.handlers;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import com.m4rc310.ml.dialogs.DialogSearch;

public class PesquisaHandler {
	@Execute
	public void execute(IEclipseContext context) {
		DialogSearch dialogSearch = ContextInjectionFactory.make(DialogSearch.class, context);
		dialogSearch.open();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}