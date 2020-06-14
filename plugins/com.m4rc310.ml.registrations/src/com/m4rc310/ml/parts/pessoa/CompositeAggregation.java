package com.m4rc310.ml.parts.pessoa;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Vinculo;
import com.m4rc310.ml.ui.parts.PartControl;

@Creatable
public class CompositeAggregation implements IComposite {

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
	Shell shell;

	@Override
	public Composite make(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(3, false));
		parent.setBackground(pc.getColor(SWT.COLOR_YELLOW));

		pc.clearMargins(parent);

		Label labelRegistration = pc.getLabel(parent, m.labelRegistration);
		Text textRegistration = pc.getText(parent, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textRegistration, 70);
		Composite stack = pc.getStackComposite(parent);
		Button buttonVincular = pc.getButton(stack, m.textVincular, e -> actionPessoa.bond());
		Button buttonDesvincular = pc.getButton(stack, m.textDesvincular, e -> actionPessoa.unbond(true));

		actionPessoa.addListener(this, ActionPessoa2.SEARCHING, e -> {
			boolean searching = e.getValue(boolean.class);
			sync.asyncExec(() -> pc.enabled(!searching, buttonVincular, buttonDesvincular, labelRegistration));
		});

		actionPessoa.addListener(this, ActionPessoa2.QUESTION_REMOVE_BOND, e -> {
			String message = e.getValue(String.class);
			boolean question = MessageDialog.openQuestion(shell, m.titleDefaultCadastroPessoa, message);
			if (question) {
				actionPessoa.unbond(false);
			}
		});

		actionPessoa.addListener(this, ActionPessoa2.ENABLE_BOND, e -> {
			boolean aggregate = e.getValue(0, boolean.class);
			Vinculo vinculo = e.getValue(1, Vinculo.class);

			pc.enabled(aggregate, buttonDesvincular);
			pc.enabled(!aggregate, buttonVincular);

			sync.asyncExec(() -> pc.toTopControl(aggregate ? buttonDesvincular : buttonVincular));
			sync.asyncExec(() -> pc.observeTextString(textRegistration, "matricula", vinculo));
		});

		actionPessoa.addListener(this, ActionPessoa2.RESET_PART, e -> {
			pc.enabled(false, buttonDesvincular, buttonVincular, labelRegistration, textRegistration);
			pc.toTopControl(buttonVincular);
		});

		return parent;
	}

}
