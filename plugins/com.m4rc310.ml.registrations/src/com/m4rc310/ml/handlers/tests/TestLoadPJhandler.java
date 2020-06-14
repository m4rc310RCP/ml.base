 
package com.m4rc310.ml.handlers.tests;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;

import com.m4rc310.ml.actions.ActionPessoa2;

public class TestLoadPJhandler {
	
	
	@Inject ActionPessoa2 actionPessoa;
	private boolean isLoaded = false;
	
	@Execute
	public void executeTest() {
	}
	
	
	public void execute() {
		this.isLoaded = !isLoaded;
		if(isLoaded) {
			actionPessoa.cancelCheckingForPJ();
		}else {
			actionPessoa.searchAndLoadPJ("75904383003570");
		}
	}
		
}