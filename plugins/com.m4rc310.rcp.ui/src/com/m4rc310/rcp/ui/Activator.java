package com.m4rc310.rcp.ui;

import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {
	public static final String	PLUGIN_ID	= "com.m4rc310.rcp.ui";
	private static Activator	plugin;
	
	public Activator() {
	}

	
	public static Activator getDefault() {
		return plugin;
	}
	
	public static IPreferenceStore getPrefStore() {
		return Activator.getDefault().getPreferenceStore();
	}
	
	public static IDialogSettings getState(final String stateSectionName) {
		return getDefault().getDialogSettingsSection(stateSectionName);
	}

	
	public IDialogSettings getDialogSettingsSection(final String sectionName) {

		final IDialogSettings dialogSettings = getDialogSettings();
		IDialogSettings section = dialogSettings.getSection(sectionName);

		if (section == null) {
			section = dialogSettings.addNewSection(sectionName);
		}

		return section;
	}
	
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}
	
}
