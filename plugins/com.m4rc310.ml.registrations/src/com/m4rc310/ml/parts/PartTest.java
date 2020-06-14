
package com.m4rc310.ml.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.parts.pessoa.CompositeAggregation;
import com.m4rc310.ml.parts.pessoa.CompositeIE;
import com.m4rc310.ml.parts.pessoa.CompositeIdentify;
import com.m4rc310.ml.parts.pessoa.CompositePF;
import com.m4rc310.ml.parts.pessoa.CompositePJ;
import com.m4rc310.ml.ui.parts.PartControl;

public class PartTest {

	@Inject
	ActionPessoa2 actionPessoa;

	@Inject
	CompositePJ compositePJ;

	@Inject
	CompositeIE compositeIE;
	
	@Inject
	private MPart part;

	@Inject PartControl pc;
	
	@Inject
	public PartTest() {

	}

	@PostConstruct
	public void postConstruct(Composite parent, CompositeIdentify identify, CompositePF compositePF,
			CompositeAggregation aggregation) {

		parent.setLayout(new GridLayout(2, false));

		identify.make(parent);
//		aggregation.make(parent);

		compositeIE.make(parent);

		Composite parentPF = compositePF.make(parent);

//		Composite parentPJ = compositePJ.make(parent);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		parentPF.setLayoutData(gd);
	
		actionPessoa.addListener(this, ActionPessoa2.ENABLE_DIRTY, e->{
			boolean dirty = e.getValue(boolean.class);
			part.setDirty(dirty);
		});
		
		actionPessoa.addListener(this, ActionPessoa2.SET_PART_TITLE, e->{
			String title = e.getValue(String.class);
			part.setLabel(title);
		});
		
		
		actionPessoa.init();
	}

	@Persist
	public void save() {
		actionPessoa.save();
	}

}