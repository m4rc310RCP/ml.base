package com.m4rc310.ml.parts.pessoa;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaFisica;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.ui.parts.PartControl;

@Creatable
public class CompositeIdentify implements IComposite{

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
	public Composite make(Composite parent_) {
//		Composite parent = pc.getComposite(parent_);
//		parent.setLayout(new GridLayout());
		
		Composite group = pc.getComposite(parent_);
		group.setLayout(new GridLayout(6, false));
		group.setBackground(pc.getColor(SWT.COLOR_CYAN));
		pc.clearMargins(group);
		Label labelCpfOrCnpj = pc.getLabel(group, m.labelCpfOrCnpj);
		Text textCC = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textCC, 170);
		textCC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				actionPessoa.writeCC(textCC.getText());
			}
		});

		Composite stack = pc.getStackComposite(group);

		Button buttonNoCC = pc.getButton(stack, m.textMoveForwardWithoutCpfOrCnpj,
				e -> actionPessoa.enterDataManually());

		Button buttonAdvance = pc.getButton(stack, m.textAdvance, e -> actionPessoa.searchAndLoad(textCC.getText()));

		Button buttonCancel = pc.getButton(stack, m.textCancel, e -> actionPessoa.cancelCheckingForPJ());

		Label labelChoosePeopleType = pc.getLabel(group, m.labelChoosePeopleType);
		ComboViewer comboViewerPessoa = pc.getComboViewer(group, SWT.DROP_DOWN | SWT.READ_ONLY);
		addComboLabelProvider(comboViewerPessoa);

		pc.setGridDataWidthHint(comboViewerPessoa.getCombo(), 100);
		Button buttonConfirm = pc.getButton(group, m.textConfirmar);

		actionPessoa.addListener(this, ActionPessoa2.IS_LOADED, e -> {
			boolean isLoaded = e.getValue(boolean.class);
			pc.toTopControl(isLoaded ? buttonCancel : buttonNoCC);
		});

		actionPessoa.addListener(this, ActionPessoa2.SET_TYPE,
				e -> sync.asyncExec(() ->comboViewerPessoa.setSelection(new StructuredSelection(e.getValue(Integer.class)))));

		actionPessoa.addListener(this, ActionPessoa2.LOAD_PF, e -> {
			PessoaFisica pf = e.getValue(PessoaFisica.class);
			sync.asyncExec(()->pc.observeTextCpfCnpj(textCC, "cpf", pf));
		});
		
		actionPessoa.addListener(this, ActionPessoa2.LOAD_PJ, e -> {
			PessoaJuridica pj = e.getValue(PessoaJuridica.class);
			sync.asyncExec(()->pc.observeTextCpfCnpj(textCC, "cnpj", pj));
//			sync.asyncExec(() -> pc.observeTextString(textCC, "cnpj", pj));
//			sync.asyncExec(() ->pc.toTopControl(buttonCancel));
		});

		actionPessoa.addListener(this, ActionPessoa2.ENABLE_MANUALLY_DATA, e -> {
			boolean manually = e.getValue(boolean.class);
			pc.enabled(!manually, textCC, labelCpfOrCnpj);
			pc.enabled(manually, comboViewerPessoa.getCombo(), labelChoosePeopleType, buttonCancel);
			pc.toTopControl(buttonCancel);
		});

		actionPessoa.addListener(this, ActionPessoa2.LOAD_TYPES, e -> {
			comboViewerPessoa.setInput(e.getListValue(0, Integer.class));
		});

		actionPessoa.addListener(this, ActionPessoa2.SEARCHING, e -> {
			Boolean searching = e.getValue(boolean.class);

			if (searching)
				sync.asyncExec(() -> pc.enabled(false, buttonAdvance, textCC, labelCpfOrCnpj));

			Cursor cursor = new Cursor(display, searching ? SWT.CURSOR_WAIT : SWT.CURSOR_ARROW);
			shell.setCursor(cursor);

		});

		actionPessoa.addListener(this, ActionPessoa2.INFORME_VALID_CC, e -> {
			boolean valid = e.getValue(boolean.class);
			pc.toTopControl(valid ? buttonAdvance : buttonNoCC);
			if (valid)
				pc.setDefaultButton(buttonAdvance);
			pc.enabled(valid, buttonAdvance);
		});

		actionPessoa.addListener(this, ActionPessoa2.RESET_PART, e -> {
			pc.enabled(false, buttonConfirm, comboViewerPessoa, buttonAdvance, labelChoosePeopleType);
			pc.enabled(true, textCC, labelCpfOrCnpj);
			textCC.setText(m.empty);
			sync.asyncExec(() -> textCC.setFocus());
			pc.toTopControl(buttonNoCC);
		});

		return group;
	}

	private void addComboLabelProvider(ComboViewer combo) {
		combo.setContentProvider(ArrayContentProvider.getInstance());
		combo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				int type = (int) element;

				switch (type) {
				case Pessoa.FISICA:
					return m.textPeopleFisica;
				case Pessoa.JURIDICA:
					return m.textPeopleJuridica;
				default:
					return m.empty;
				}
			}
		});
	}


}
