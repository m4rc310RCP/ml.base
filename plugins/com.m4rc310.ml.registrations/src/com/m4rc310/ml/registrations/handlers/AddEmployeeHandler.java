 
package com.m4rc310.ml.registrations.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.CanExecute;

public class AddEmployeeHandler {
	@Execute
	public void execute() {
		
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}