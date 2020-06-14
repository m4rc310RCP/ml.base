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

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.Version;

/**
 * This class holds information about the current application. Parts copied from
 * org.eclipse.ui.internal.WorkbenchPlugin;
 *
 */

@Creatable
@Singleton
public class E4ApplicationInfo implements IE4ApplicationInfo {

	private E4ProductInfo productInfo;

	@PostConstruct
	protected void init() {
		// initialize ProductInfo
		getProductInfo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.rhe.e4test.ui.IE4ApplicationInfo#getAppName()
	 */
	@Override
	public String getAppName() {
		return getProductInfo().getAppName();
	}

	/**
	 * Return the application version, as defined by the product.
	 * 
	 * @return the application version, or the empty version.
	 * @see org.eclipse.swt.widgets.Display#setAppVersion
	 * @see Version#emptyVersion
	 * @since 3.6
	 */
	@Override
	public String getAppVersion() {
		return getProductInfo().getAppVersion();
	}

	@Override
	public ImageDescriptor getAboutImage() {
		return getProductInfo().getAboutImage();
	}

	@Override
	public String getAboutText() {
		return getProductInfo().getAboutText();
	}

	/**
	 * Returns the name of the product.
	 * 
	 * @return the product name, or <code>null</code> if none
	 * @since 3.0
	 */
	@Override
	public String getProductName() {
		return getProductInfo().getProductName();
	}

	/**
	 * Returns the image descriptors for the window image to use for this product.
	 * 
	 * @return an array of the image descriptors for the window image, or
	 *         <code>null</code> if none
	 * @since 3.0
	 */
	@Override
	public ImageDescriptor[] getWindowImages() {
		return getProductInfo().getWindowImages();
	}

	/**
	 * Returns an instance that describes this plugin's product (formerly "primary
	 * plugin").
	 * 
	 * @return ProductInfo the product info for the receiver
	 */
	private E4ProductInfo getProductInfo() {
		if (productInfo == null) {
			productInfo = new E4ProductInfo(Platform.getProduct());
		}
		return productInfo;
	}

}
