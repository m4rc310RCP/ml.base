 
package com.m4rc310.ml.base.parts;

import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.widgets.Composite;

import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Processo;

public class PartSale {
	
	@Inject MPart part;
	
	@Inject @Translation M m;
	
	@Inject
	public PartSale() {
		
	}
	
	@PostConstruct
	public void postConstruct(Composite parent) {
		Processo pro = (Processo) part.getObject();
		String title = MessageFormat.format(m.titlePartSales, pro.getId()); //NLS.bind(m.titlePartSales, pro.getId());
		part.setLabel(title);
	}
	
	
	@PreDestroy
	public void preDestroy() {
		
	}
	
	
	@Focus
	public void onFocus() {
		
	}
	
	
	@Persist
	public void save() {
		
	}
	
}