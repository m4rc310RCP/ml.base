package com.m4rc310.ml.statusbar.actions;

import java.text.MessageFormat;
import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.swt.graphics.Image;
import org.eclipse.wb.swt.ResourceManager;

import com.m4rc310.ml.ui.CronUtils;
import com.m4rc310.ml.ui.actions.MAction;



@Creatable
@Singleton
public class ActionStatusBar extends MAction {
	public static final String SHOW_VERSION_INFO = "show_version_info";
	public static final String SEND_DATE_TIME = "send_date_time";
	public static final String LEFT_MESSAGE = "left_message";
	
	public enum IconType{
		
		EMOTICON_FACE_ANGEL("icons/emoticons/face-angel.png"),
		EMOTICON_FACE_CRYING("icons/emoticons/face-crying.png"),
		EMOTICON_FACE_DEVILISH("icons/emoticons/face-devilish.png"),
		EMOTICON_FACE_GLASSES("icons/emoticons/face-glasses.png"),
		EMOTICON_FACE_GRIN("icons/emoticons/face-grin.png"),
		EMOTICON_FACE_KISS("icons/emoticons/face-kiss.png"),
		EMOTICON_FACE_PLAIN("icons/emoticons/face-plain.png"),
		EMOTICON_FACE_SAD("icons/emoticons/face-sad.png"),
		EMOTICON_FACE_SMILE_BIG("icons/emoticons/face-smile-big.png"),
		EMOTICON_FACE_SMILE("icons/emoticons/face-smile.png"),
		EMOTICON_FACE_SURPRISE("icons/emoticons/face-surprise.png"),
		EMOTICON_FACE_WINK("icons/emoticons/face-wink.png"),
		
		WAIT("icons/hourglass_select_remain.png"),
		EXCLAMATION("icons/exclamation.png"),
		ERROR("icons/error.png");
		
		private String path;
		
		private IconType(String path) {
			this.path = path;
		}
		
		public String getPath() {
			return path;
		}
	}
	
	private Image iconDefault = ResourceManager.getPluginImage("com.m4rc310.ml.statusbar", "icons/shading.png");
	
	@Inject CronUtils cron;
	
	public  void setLeftMessage(String message, Object... args) {
		setLeftMessage(iconDefault, message, args);
	}
	
	public  void setLeftMessage(IconType iconType, String message, Object... args) {
		Image icon = ResourceManager.getPluginImage("com.m4rc310.ml.statusbar", iconType.getPath());
		setLeftMessage(icon, message, args);
	}
	public  void setLeftMessage(Image icon, String message, Object... args) {
//		System.out.println(message);
		if(icon==null || message == null) {
			return ;
		}
		
		message = MessageFormat.format(message, args);
		fire(LEFT_MESSAGE, icon, message);
	}
	
	
	
	public void setVersionInfo(String info) {
		fire(SHOW_VERSION_INFO, info);
	}
	
	public void startWatch() {
		cron.cron("0/10 * * * * ?", ()->{
			fire(SEND_DATE_TIME, new Date());
		});
		fire(SEND_DATE_TIME, new Date());
	}
	
}
