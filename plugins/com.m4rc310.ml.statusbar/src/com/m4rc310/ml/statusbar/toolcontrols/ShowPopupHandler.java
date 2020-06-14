 
package com.m4rc310.ml.statusbar.toolcontrols;

import com.m4rc310.rcp.popup.notifications.MPopupNotification;
import org.eclipse.e4.core.di.annotations.Execute;

public class ShowPopupHandler {
	@Execute
	public void execute(MPopupNotification popup) {
		popup.show("Teste Popup", 500);
	}
		
}