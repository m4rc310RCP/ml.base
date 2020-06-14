package com.m4rc310.ml.base2.addons;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.osgi.service.event.Event;

import com.m4rc310.ml.base2.i18n.M;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar.IconType;
//import com.m4rc310.ml.statusbar.actions.ActionStatusBar;


@SuppressWarnings("restriction")
public class AddonStartApp {
	
	@Inject ActionStatusBar status;
	
	@Inject @Translation M m;

	@Inject
	@Optional
	public void applicationStarted(
			@EventTopic(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE) Event event) {
		status.setVersionInfo("1232");
		status.startWatch();
		status.setLeftMessage(IconType.EMOTICON_FACE_WINK, m.textWelcomeMessage);
	}

}
