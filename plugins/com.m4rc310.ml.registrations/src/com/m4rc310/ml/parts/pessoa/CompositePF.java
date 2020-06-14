package com.m4rc310.ml.parts.pessoa;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.observable.IChangeListener;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.actions.ActionPessoa2.EnumValues;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaFisica;
import com.m4rc310.ml.ui.parts.PartControl;

@Creatable
public class CompositePF implements IComposite {

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

		group.setBackground(pc.getColor(SWT.COLOR_DARK_GREEN));

		Composite c = pc.getComposite(group);
		c.setLayout(new GridLayout());
		pc.clearMargins(c);

		pc.getLabel(c, m.labelNome);
		Text textNome = pc.getText(c, m.empty);
		pc.setGridDataWidthHint(textNome, 280);

		c = pc.getComposite(group);
		c.setLayout(new GridLayout(1, false));
		pc.clearMargins(c);

		Label label = pc.getLabel(c, m.labelDataNascimento);
		GridData gd = new GridData();
//		gd.horizontalSpan=2;
		label.setLayoutData(gd);

		Text textDataNascimento = pc.getText(c, m.empty, SWT.CENTER | SWT.BORDER);
		pc.setGridDataWidthHint(textDataNascimento, 100);

		pc.configureDateField("dd/MM/yyyy", textDataNascimento);

//		Label labelAge = pc.getLabel(c, m.empty);
//		pc.setGridDataWidthHint(labelAge, 40);

		pc.getButton(parent, m.textAdvance, e -> {

		});

		c = pc.getComposite(group);
		c.setLayout(new GridLayout(1, false));
		pc.clearMargins(c);

		Label labelSexo = pc.getLabel(c, m.labelSexo);

//		String[] listGeneros = new String[] { m.textSexoMasculino, m.textSexoFeminino };
//		Composite sexoParent = pc.getComposite(c);
//		sexoParent.setLayout(new GridLayout(listGeneros.length, false));

//		SelectObservableValue<Integer> selectedRadioButtonObservable = new SelectObservableValue<>();
//		int i = 0;
//		for (String ssexo : listGeneros) {
//			Button button = pc.getButton(sexoParent, ssexo, SWT.RADIO);
////			selectedRadioButtonObservable.addOption(i++, WidgetProperties.buttonSelection().observe(button));
//		}

		ComboViewer comboViewerSexo = pc.getComboViewer(c, SWT.READ_ONLY);
		comboViewerSexo.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerSexo.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				int value = (int) element;
				String sexo = "";
				switch (value) {
				case Pessoa.SEXO_FEMININO:
					sexo = m.textSexoFeminino;
					break;
				case Pessoa.SEXO_MASCULINO:
					sexo = m.textSexoMasculino;
					break;
				}
				return sexo;
			}
		});
		pc.setGridDataWidthHint(comboViewerSexo.getCombo(), 100);
//		
		c = pc.getComposite(group);
		c.setLayout(new GridLayout(1, false));
		pc.clearMargins(c);

		Label labelEstadoCivil = pc.getLabel(c, m.labelEstadoCivil);
		ComboViewer comboViewerEstadoCivil = pc.getComboViewer(c, SWT.READ_ONLY);
		comboViewerEstadoCivil.setContentProvider(ArrayContentProvider.getInstance());
		comboViewerEstadoCivil.setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				int value = (int) element;
				switch (value) {
				case Pessoa.ESTADO_CIVIL_CASADO:
					return m.textEstadoCivilCasado;
				case Pessoa.ESTADO_CIVIL_DIVORCIADO:
					return m.textEstadoCivilDivorciado;
				case Pessoa.ESTADO_CIVIL_SEPARADO_JUDICIAL:
					return m.textEstadoCivilSeparadoJudicialmente;
				case Pessoa.ESTADO_CIVIL_SOLTEIRO:
					return m.textEstadoCivilSolteiro;
				case Pessoa.ESTADO_CIVIL_UNIAO_ESTAVEL:
					return m.textEstadoCivilUniaoEstavel;
				case Pessoa.ESTADO_CIVIL_VIUVO:
					return m.textEstadoCivilViuvo;
				}

				return super.getText(element);
			}
		});
		pc.setGridDataWidthHint(comboViewerEstadoCivil.getCombo(), 130);

		actionPessoa.addListener(this, ActionPessoa2.LOAD_VALUES, e -> {
			EnumValues typeValue = e.getValue(0, EnumValues.class);
			switch (typeValue) {
			case LIST_SEXO:
				List<Integer> values = e.getListValue(1, Integer.class);
				comboViewerSexo.setInput(values);
				break;
			case LIST_ESTADO_CIVIL:
				values = e.getListValue(1, Integer.class);
				comboViewerEstadoCivil.setInput(values);
				break;
			}

		});

		actionPessoa.addListener(this, ActionPessoa2.LOAD_PF, e -> {
			PessoaFisica pf = e.getValue(PessoaFisica.class);

			IChangeListener listener = event -> {
				actionPessoa.changePF();
			};

			pc.removeObserverListener(listener);

			pc.observeTextDate(textDataNascimento, "nascimento", "dd/MM/yyyy", pf);
			pc.observeTextString(textNome, "nome", pf);
			pc.observeComboList(comboViewerSexo, "sexo", pf);
			pc.observeComboList(comboViewerEstadoCivil, "estadoCivil", pf);

			pc.addObserverListener(listener);

			if (pf != null) {
				sync.asyncExec(() -> textNome.setFocus());
			}
		});

		return group;
	}

}
