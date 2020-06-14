package com.m4rc310.rcp.popup.notifications;
import org.eclipse.swt.graphics.Image;

public interface IPopupNotification {
	void show(MNotificationPopup popup);
	
	void show(String text);
	void show(String title, String text);
	
	void show(Image icon, String text);
	void show(Image icon, String title, String text);

	void show(String text, int autoCloseMiliseconds);
	void show(String title, String text, int autoCloseMiliseconds);
	
	void show(Image icon, String text, int autoCloseMiliseconds);
	void show(Image icon, String title, String text, int autoCloseMiliseconds);
}
