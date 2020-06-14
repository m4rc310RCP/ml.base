 
package com.m4rc310.ml.base.handlers;

import com.m4rc310.rcp.popup.notifications.MPopupNotification;
import com.m4rc310.rcp.ui.utils.appinfo.E4ApplicationInfo;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class UpdateHandler {
	@Execute
	public void execute(MPopupNotification popup, E4ApplicationInfo app) {
		String info = String.format("A versão atualmente instalada (%s) é a mais recente!", app.getAppVersion());
		popup.show("Não há atualizações Disponíveis", info);
	}
	
	@CanExecute
	public boolean canExecute() {
		
		return true;
	}
		
}