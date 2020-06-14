
package com.m4rc310.ml.parts;

import java.util.Arrays;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPeopleRegistration;
import com.m4rc310.ml.actions.ActionPeopleRegistration.EnumPartChange;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Atividade;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar.IconType;
import com.m4rc310.ml.ui.parts.PartControl;


@Deprecated
public class PartPeople {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	UISynchronize sync;

	@Inject
	ActionPeopleRegistration action;

	@Inject
	ActionStatusBar actionStatus;

	@PostConstruct
	public void post(Composite parent_, Shell shell, Display display) {
		Composite parent = new Composite(parent_, SWT.NONE);
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		parent.setLayout(new GridLayout());

		CTabFolder folder = pc.createCTabFolder(parent);
		folder.setBorderVisible(true);
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		folder.setSelectionBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		ScrolledComposite sc = pc.getScrolledComposite(folder);

		Image icon = pc.getImage("com.m4rc310.ml.registrations", "icons/user_blue_information.png");
		CTabItem item = pc.createCTabItem(folder, m.textPessonalInformations, icon);

		Composite content = pc.getComposite(sc);

		createContentForPersonalInfo(content);

		pc.setContentToScrolled(content);
		item.setControl(sc);

		folder.setSelection(item);

		action.addListener(this, ActionPeopleRegistration.INFORM_ERROR, e -> {
			String error = e.getValue(String.class);
			actionStatus.setLeftMessage(IconType.EMOTICON_FACE_SURPRISE, error);
		});

		action.addListener(this, ActionPeopleRegistration.INFORM_LOADING, e -> {
//			sync.asyncExec(() -> {
			Cursor cursor = new Cursor(display, e.getValue(0, boolean.class) ? SWT.CURSOR_WAIT : SWT.CURSOR_ARROW);
			shell.setCursor(cursor);

			String text = e.getValue(1, String.class);
			actionStatus.setLeftMessage(text);
//			});
		});

		action.init();
	}

	private void createContentForPersonalInfo(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(2, false));
		Group group = pc.getGroup(parent);
		group.setLayout(new GridLayout(5, false));

		pc.getLabel(group, m.labelId);

		Text textId = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textId, 50);
		pc.enabled(false, textId);

		pc.getLabel(group, m.labelCpfOrCnpj);

		Text textCpf = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textCpf, 130);
//		pc.configureVerifyValues("[0-9]|[-./]", textCpf);
		pc.addTextModifyListener(textCpf, e -> action.readingCpfCnpj(textCpf.getText()));

		Composite buttonStack = pc.getStackComposite(group);
		Button buttonAdvance = pc.getButton(buttonStack, m.textAdvance, e -> {
			sync.asyncExec(() -> action.verifyCpfCnpj(textCpf.getText()));
		});

		Button buttonAdvanceNoCpf = pc.getButton(buttonStack, m.textMoveForwardWithoutCpfOrCnpj);

		Composite p1 = pc.getComposite(parent);
		p1.setLayout(new GridLayout(2, false));

		Label label = pc.getLabel(p1, m.labelChoosePeopleType);
		ComboViewer comboViewer = pc.getComboViewer(p1, SWT.READ_ONLY);
		GridData gd = new GridData();
		gd.widthHint = 130;

		Combo combo = comboViewer.getCombo();
		combo.setLayoutData(gd);
		pc.enabled(false, combo);

		Composite parentPJ = pc.getStackComposite(parent);
		contentOfPessoaJuridica(parentPJ);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		parentPJ.setLayoutData(gd);

		Composite parentPF = pc.getStackComposite(parent);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;

		pc.getLabel(parentPF, m.textCode);

		parentPF.setLayoutData(gd);

//		Composite pessoaFisicaComposite = contentOfPessoaJuridica(parent);

		comboViewer.setLabelProvider(new LabelProvider() {
			public String getText(Object element) {
				int value = (int) element;
				switch (value) {
				case Pessoa.FISICA:
					return m.textPeopleFisica;
				case Pessoa.JURIDICA:
					return m.textPeopleJuridica;
				default:
					return m.empty;
				}
			}
		});

		comboViewer.setContentProvider(ArrayContentProvider.getInstance());

		action.addListener(this, ActionPeopleRegistration.LOAD_PEOPLE_TYPES, e -> {
			sync.asyncExec(() -> comboViewer.setInput(e.getListValue(0, Integer.class)));
		});

		action.addListener(this, ActionPeopleRegistration.LOAD_PESSOA_JURIDICA,
				e -> pc.observeTextString(textCpf, "cnpj", e.getValue(PessoaJuridica.class)));

		action.addListener(this, ActionPeopleRegistration.SET_PEOPLE_TYPES, e -> {
			Integer pessoa = e.getValue(Integer.class);
			comboViewer.setSelection(new StructuredSelection(pessoa));
			switch (pessoa) {
			case Pessoa.FISICA:
				pc.toTopControl(parentPF);
				break;
			case Pessoa.JURIDICA:
				pc.toTopControl(parentPJ);
				break;
			default:
				break;
			}
		});

		action.addListener(this, ActionPeopleRegistration.CHANGE_PART, e -> {

			sync.asyncExec(() -> {

				switch (e.getValue(0, EnumPartChange.class)) {
				case EMPTY_CPF:
					pc.toTopControl(buttonAdvanceNoCpf);
					pc.setDefaultButton(buttonAdvanceNoCpf);
					pc.enabled(true, buttonAdvanceNoCpf, label);
					break;
				case INVALID_CPF:
					pc.toTopControl(buttonAdvanceNoCpf);
					pc.enabled(false, buttonAdvanceNoCpf, label);
					break;
				case VALID_CPF:
					pc.toTopControl(buttonAdvance);
					pc.enabled(true, buttonAdvance);
					pc.setDefaultButton(buttonAdvance);
					break;
				case ON_FOCUS:
					pc.enabled(true, textCpf);
					sync.asyncExec(() -> textCpf.setFocus());
					break;
				case LOADING_PEOPLE:
					pc.enabled(false, textCpf, buttonAdvance);
					break;
				default:
					break;
				}
			});
		});
	}

	private void contentOfPessoaJuridica(Composite parent_) {
		parent_.setLayout(new GridLayout(2, false));
		pc.clearMargins(parent_);

		Group parent = pc.getGroup(parent_);
		parent.setLayout(new GridLayout(2, false));
		pc.clearMargins(parent);

		Composite l1 = pc.getComposite(parent);
		l1.setLayout(new GridLayout(3, false));

		Composite p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p, parent_);
		pc.getLabel(p, m.labelRazaoSocial);
		Text textNome = pc.getText(p, m.empty);
		pc.setGridDataWidthHint(textNome, 300);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelFantasia);
		Text textFantasia = pc.getText(p, m.empty);
		pc.setGridDataWidthHint(textFantasia, 250);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelSituacao);
		Text textSituacao = pc.getText(p, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textSituacao, 130);

		TreeViewer treeQSA = new TreeViewer(parent);
		GridData gd = new GridData();
		gd.widthHint = 180;
		gd.heightHint = 200;
		gd.horizontalSpan = 2;
		
		treeQSA.getTree().setLayoutData(gd);
		
		
		l1 = pc.getComposite(parent);
		l1.setLayout(new GridLayout(3, false));
		 gd = new GridData();
		 gd.horizontalSpan = 2;
		 l1.setLayoutData(gd);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelNaturezaJuridica);
		Text textNatureza = pc.getText(p, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textNatureza, 240);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelDataAbertura);
		Text textAbertura = pc.getText(p, m.empty, SWT.BORDER | SWT.CENTER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		textAbertura.setLayoutData(gd);
		pc.setGridDataWidthHint(textAbertura, 100);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelCapitalSocial);
		Text textCapitalSocial = pc.getText(p, m.empty);
		pc.setGridDataWidthHint(textCapitalSocial, 120);

		l1 = pc.getComposite(parent);
		l1.setLayout(new GridLayout(3, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		l1.setLayoutData(gd);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		pc.clearMargins(p);
		pc.getLabel(p, m.labelTipo);
		Text textTipo = pc.getText(p, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textTipo, 120);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		p.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pc.clearMargins(p);
		pc.getLabel(p, m.labelAtividadePrincipal);
		Text textAtividadePrincipal = pc.getText(p, m.empty);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		textAtividadePrincipal.setLayoutData(gd);

		l1 = pc.getComposite(parent);
		l1.setLayout(new GridLayout(1, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		l1.setLayoutData(gd);

		p = pc.getComposite(l1);
		p.setLayout(new GridLayout());
		p.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pc.clearMargins(p);

		pc.getLabel(p, m.labelAtividadesSecundarias);

		TableViewer tableViewer = new TableViewer(p, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 100;

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);

		table.setLayoutData(gd);

		pc.createCollumn(tableViewer, SWT.CENTER, m.textCode, 100);
		pc.createCollumn(tableViewer, SWT.NONE, m.textDescricao, 400);

		pc.editable(false, textFantasia, textNome, textSituacao, textTipo, textAbertura, textNatureza);

		Group parentRigth = pc.getGroup(parent_);
		parentRigth.setLayout(new GridLayout(1, false));
		gd = new GridData(GridData.FILL_VERTICAL);
		parentRigth.setLayoutData(gd);
//		pc.getLabel(parentRigth, m.labelQuadroSocios);

		TreeViewer treeQSA2 = new TreeViewer(parentRigth);
		treeQSA.setContentProvider(new ITreeContentProvider() {

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof QualGrupo) {
					return true;
				}
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object element) {

				if (element instanceof Map) {
					return ArrayContentProvider.getInstance().getElements(((Map<?, ?>) element).values());
				}

				return ArrayContentProvider.getInstance().getElements("");
			}

			@Override
			public Object[] getChildren(Object element) {
				if (element instanceof QualGrupo) {
					QualGrupo g = (QualGrupo) element;
					return ArrayContentProvider.getInstance().getElements(g.getSocios());
				}

				return null;
			}
		});

		treeQSA.getTree().setHeaderVisible(true);
		treeQSA.getTree().setLinesVisible(true);

//		pc.createColumn(treeQSA, m.textQual, 100, SWT.NONE, new StyledCellLabelProvider() {
//			@Override
//			public void update(ViewerCell cell) {
//				if (cell.getElement() instanceof QualGrupo) {
//					QualGrupo grupo = (QualGrupo) cell.getElement();
//					cell.setText(grupo.getCodigo());
//				}
//				super.update(cell);
//			}
//		});

		pc.createColumn(treeQSA, m.textQsa, 300, SWT.NONE, new StyledCellLabelProvider() {
			@Override
			public void update(ViewerCell cell) {

				Object element = cell.getElement();

				if (element instanceof QualGrupo) {
					QualGrupo grupo = (QualGrupo) element;
					String descricao = String.format("%s (%d)", grupo.getDescricao(), grupo.getSocios().size());
					cell.setText(descricao);
				}

				if (element instanceof Socio) {
					Socio socio = (Socio) element;
					cell.setText(socio.getNome());
				}

				super.update(cell);
			}
		});

		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 330;
		treeQSA.getTree().setLayoutData(gd);

		action.addListener(this, ActionPeopleRegistration.LOAD_PESSOA_JURIDICA, e -> {
			PessoaJuridica pj = e.getValue(PessoaJuridica.class);

			pc.observeTextString(textNome, "nome", pj);
			pc.observeTextString(textFantasia, "fantasia", pj);
			pc.observeTextString(textSituacao, "situacao", pj);
			pc.observeTextString(textNatureza, "naturezaJuridica", pj);
			pc.observeTextString(textAbertura, "abertura", pj);
			pc.observeTextString(textTipo, "tipo", pj);
			pc.observeTextString(textCapitalSocial, "capitalSocial", pj);

			pc.registerViewerSupport(PessoaJuridica.class, tableViewer, Arrays.asList(), "code", "text");
			textAtividadePrincipal.setText(m.empty);

			if (pj != null) {
				if (pj.getAtividadePrincipal() != null) {
					Atividade atividade = pj.getAtividadePrincipal().get(0);
					String text = String.format("%s - %s", atividade.getCode(), atividade.getText());
					textAtividadePrincipal.setText(text);
				}
				pc.registerViewerSupport(pj.getClass(), tableViewer, pj.getAtividadesSecundarias(), "code", "text");

			}
		});

		action.addListener(this, ActionPeopleRegistration.LOAD_QUAL_GRUPO, e -> {
			@SuppressWarnings("unchecked")
			Map<String, QualGrupo> mapSocios = (Map<String, QualGrupo>) e.getValue();
			treeQSA.setInput(mapSocios);

		});
	}

	@PreDestroy
	public void preDestroy() {
		action.closeing();
		action.removeListeners(this);
	}

	@Focus
	public void onFocus() {

	}

	@Persist
	public void save() {

	}

}