 
package com.m4rc310.ml.base.handlers;

import org.eclipse.e4.core.di.annotations.Execute;

import com.m4rc310.ml.base.actions.ActionSales;

import org.eclipse.e4.core.di.annotations.CanExecute;

public class NewSaleHandler {
	@Execute
	public void execute(ActionSales action) {
		action.newSale();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		return true;
	}
		
}