/* 
 * Fakturama - Free Invoicing Software - http://www.fakturama.org
 * 
 * Copyright (C) 2016 www.fakturama.org
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     The Fakturama Team - initial API and implementation
 */

package com.m4rc310.rcp.ui.utils.appinfo;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Ralf
 *
 */
public interface IE4ApplicationInfo {

	/**
	 * Returns the application name.
	 * <p>
	 * Note this is never shown to the user.
	 * It is used to initialize the SWT Display.
	 * On Motif, for example, this can be used
	 * to set the name used for resource lookup.
	 * </p>
	 *
	 * @return the application name, or <code>null</code>
	 * @see org.eclipse.swt.widgets.Display#setAppName
	 * @since 3.0
	 */
	String getAppName();

	ImageDescriptor[] getWindowImages();

	String getProductName();

	String getAppVersion();

	ImageDescriptor getAboutImage();

	String getAboutText();

}