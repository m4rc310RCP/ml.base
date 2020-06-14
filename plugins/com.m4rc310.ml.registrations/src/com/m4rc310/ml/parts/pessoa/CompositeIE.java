package com.m4rc310.ml.parts.pessoa;

import javax.inject.Inject;

import org.brazilutils.br.uf.UF;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.ui.parts.PartControl;

@Creatable
public class CompositeIE implements IComposite {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	UISynchronize sync;

	@Inject
	ActionPessoa2 actionPessoa;

	@Inject
	Display display;

	@Inject
	Shell shell;

	@Override
	public Composite make(Composite parent) {
		Composite group = pc.getComposite(parent);
		group.setLayout(new GridLayout(5, false));
		group.setBackground(pc.getColor(SWT.COLOR_RED));
		
		pc.clearMargins(group);

		pc.getLabel(group, m.labelUf);

		ComboViewer comboViewerUF = pc.getComboViewer(group, SWT.READ_ONLY);
		pc.setGridDataWidthHint(comboViewerUF.getCombo(), 50);
		comboViewerUF.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerUF.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				if (element instanceof UF) {
					UF uf = (UF) element;
					return uf.getSigla();
				}
				return super.getText(element);
			}
		});

		Label labelIE = pc.getLabel(group, m.labelIe);
		Text textIE = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textIE, 130);

		Button buttonAddIE = pc.getButton(group, m.textAdd);

		pc.addTextModifyListener(textIE, e -> {
			IStructuredSelection sts = (IStructuredSelection) comboViewerUF.getSelection();
			String sie = ((Text) e.widget).getText();
			actionPessoa.writeIE((UF) sts.getFirstElement(), sie);
		});

		comboViewerUF.addSelectionChangedListener(event -> {
			IStructuredSelection sts = (IStructuredSelection) event.getSelection();
			if (!sts.isEmpty()) {
				actionPessoa.writeIE((UF) sts.getFirstElement(), textIE.getText());
			}
		});

		actionPessoa.addListener(this, ActionPessoa2.SET_UF, e -> {
			sync.asyncExec(() -> comboViewerUF.setSelection(new StructuredSelection(e.getValue())));
		});

		actionPessoa.addListener(this, ActionPessoa2.ENABLE_IE, e -> {
			pc.enabled(e.getValue(boolean.class), labelIE, textIE);
		});

		actionPessoa.addListener(this, ActionPessoa2.INFORME_VALID_IE, e -> {
			boolean valid = e.getValue(boolean.class);
			pc.enabled(valid, buttonAddIE);
			if (valid) {
				pc.setDefaultButton(buttonAddIE);
			}
		});

		actionPessoa.addListener(this, ActionPessoa2.RESET_PART, e -> {
			sync.asyncExec(() -> comboViewerUF.setSelection(new StructuredSelection()));
		});
		
		actionPessoa.addListener(this, ActionPessoa2.LOAD_UFS, e -> {
			comboViewerUF.setInput(e.getValue(0));
		});

		return group;
	}

}
