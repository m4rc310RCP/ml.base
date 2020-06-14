 
package com.m4rc310.ml.handlers;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

import com.m4rc310.ml.ui.parts.PartControl;

public class AddClerkHandler {
	@Execute
	
	public void execute(IEclipseContext context, PartControl pc) {
		pc.show("com.m4rc310.ml.registrations.partdescriptor.part.pessoa");
//		DialogClerk dialog = ContextInjectionFactory.make(DialogClerk.class, context);
//		dialog.open();
	}
	
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}