 
package com.m4rc310.ml.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;

import com.m4rc310.ml.actions.ActionPessoa;

public class CancelarCadastroPessoaHandler {
	
	private ActionPessoa action;

	@Execute
	public void execute() {
		action.cancelar();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		return action!=null;
	}
	
	@Inject @Optional
	public void receiveAction(@UIEventTopic(ActionPessoa.SEND_ACTION_REGISTRATION) ActionPessoa action ) {
		this.action = action;
	}
	
		
}