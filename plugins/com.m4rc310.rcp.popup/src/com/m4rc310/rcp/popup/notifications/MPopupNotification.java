package com.m4rc310.rcp.popup.notifications;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

@Creatable
public class MPopupNotification implements IPopupNotification {

//	private final Image imageDefault = ResourceManager.getPluginImage("com.m4rc310.cipa.rcp.notications", "icons/Interface-66.png");

	@Override
	public void show(String text) {
		show("-", text);
	}

	@Override
	public void show(String title, String text) {
		show(null,title, text, 0);
	}

	@Override
	public void show(Image icon, String text) {
		show(icon, text, 0);
	}

	@Override
	public void show(Image icon, String title, String text) {
		show(icon,title,text, 0);
	}

	@Override
	public void show(String text, int autoCloseMiliseconds) {
		show(null,"-",text, autoCloseMiliseconds);
	}

	@Override
	public void show(String title, String text, int autoCloseMiliseconds) {
		show(null, title, text, autoCloseMiliseconds);
	}

	@Override
	public void show(Image icon, String text, int autoCloseMiliseconds) {
		show(icon, "-", text, autoCloseMiliseconds);
	}
	
	
	

	@Override
	public void show(Image icon, String title, String text, int autoCloseMiliseconds) {
		Runnable runnable = new Runnable() {
			public void run() {
				NotificationPopUpUI popup = new NotificationPopUpUI(Display.getCurrent(),icon,title,text) {
				};
				popup.open();
			}
		};
		runnable.run();
	}

	@Override
	public void show(MNotificationPopup popup) {
		Runnable runnable = new Runnable() {
			public void run() {
				popup.open();
			}
		};
		runnable.run();
	}

}
