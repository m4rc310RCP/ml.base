package com.m4rc310.ml.parts;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
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

import com.m4rc310.ml.actions.ActionPessoa;
import com.m4rc310.ml.actions.ActionPessoa.EnumPart;
import com.m4rc310.ml.actions.ActionSearch;
import com.m4rc310.ml.actions.ActionSearch.EnumSearch;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.models.Vinculo;
import com.m4rc310.ml.statusbar.actions.ActionStatusBar;
import com.m4rc310.ml.ui.parts.PartControl;

public class PartCadastroPessoa {
	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	UISynchronize sync;

	@Inject
	ActionPessoa actionPessoa;

	@Inject
	ActionStatusBar status;

	@Inject
	Shell shell;
	@Inject
	Display display;

	@Inject
	MPart part;

	@Inject
	ActionSearch actionSearch;

	@PostConstruct
	public void post(Composite parent_) {
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

		Composite ct = pc.getComposite(content);
		ct.setLayout(new GridLayout());

		createTopLine(ct);

		pc.setContentToScrolled(content);
		item.setControl(sc);

		folder.setSelection(item);
		
		actionSearch.addListener(this, ActionSearch.SEARCH_ACTION, e->{
			EnumSearch enumSearch = e.getValue(0, EnumSearch.class);
			
			switch (enumSearch) {
			case CONFIGURE_TABLE_VIEW:
				TableViewer tableViewer = e.getValue(1, TableViewer.class);
				tableViewer.setContentProvider(new ArrayContentProvider());
				

				pc.createCollumn(tableViewer, SWT.CENTER, m.textCnpj, 150, new StyledCellLabelProvider() {
					public void update(ViewerCell cell) {
						Object element = cell.getElement();
						if (element instanceof PessoaJuridica) {
							PessoaJuridica pj = (PessoaJuridica) element;
							cell.setText(pj.getCnpj());
						}
						super.update(cell);
					}
				});
			
				pc.createCollumn(tableViewer, SWT.NONE, m.textNome, 250, new StyledCellLabelProvider() {
					public void update(ViewerCell cell) {
						Object element = cell.getElement();
						if (element instanceof PessoaJuridica) {
							PessoaJuridica pj = (PessoaJuridica) element;
							cell.setText(pj.getNome());
						}
						super.update(cell);
					}
				});
				
				
				pc.createCollumn(tableViewer, SWT.NONE, m.textMunicipio, 200, new StyledCellLabelProvider() {
					public void update(ViewerCell cell) {
						Object element = cell.getElement();
						if (element instanceof PessoaJuridica) {
							PessoaJuridica pj = (PessoaJuridica) element;
							cell.setText(pj.getMunicipio());
						}
						super.update(cell);
					}
				});
				break;
			default:
				break;
			}
			
		});

		actionPessoa.init();

	}

	private void createTopLine(Composite parent) {
		parent.setLayout(new GridLayout(2, false));

		Group group = pc.getGroup(parent);
		group.setLayout(new GridLayout(6, false));

		GridData gd = new GridData();

		group.setLayoutData(gd);

		Label labelCC = pc.getLabel(group, m.labelCpfOrCnpj);
		Text textCC = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textCC, 150);

		textCC.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				actionPessoa.escreverCpfCnpj(textCC.getText());
			}
		});

//		pc.addTextModifyListener(textCC, e -> actionPessoa.escreverCpfCnpj(textCC.getText()));

		Composite stackButtons1 = pc.getStackComposite(group);
		Button buttonAvancar = pc.getButton(stackButtons1, m.textAdvance, e -> actionPessoa.avancar(textCC.getText()));
		Button buttonCancelar = pc.getButton(stackButtons1, m.textCancel, arg -> actionPessoa.cancelar());
		Button buttonAvancarSemCC = pc.getButton(stackButtons1, m.textMoveForwardWithoutCpfOrCnpj,
				e -> actionPessoa.avancarSemCC());

		Label labelMudarPessoa = pc.getLabel(group, m.labelChoosePeopleType);
		ComboViewer comboViewer = pc.getComboViewer(group, SWT.READ_ONLY);
		comboViewer.setContentProvider(ArrayContentProvider.getInstance());
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

		comboViewer.addSelectionChangedListener(event -> {
			IStructuredSelection sts = (IStructuredSelection) event.getSelection();
			if (!sts.isEmpty()) {
				actionPessoa.mudarPartPessoa((int) sts.getFirstElement());
			}
		});

		Combo comboMudarPessoa = comboViewer.getCombo();
		pc.setGridDataWidthHint(comboMudarPessoa, 150);

		Button buttonConfirmar = pc.getButton(group, m.textConfirmar);

		group = pc.getGroup(parent);
		group.setLayout(new GridLayout(3, false));
		group.setLayoutData(new GridData(GridData.FILL_BOTH));

		Label labelMatricula = pc.getLabel(group, m.labelMatricula);
		Text textMatricula = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);

		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.widthHint = 50;
		textMatricula.setLayoutData(gd);

//		Text textDataVinculo = pc.getText(group, m.empty, SWT.BORDER | SWT.CENTER);

		pc.editable(false, textMatricula);
		pc.enabled(false, labelMatricula);

		Composite buttonStack = pc.getStackComposite(group);
		Button buttonVincular = pc.getButton(buttonStack, m.textVincular, e -> actionPessoa.vincular(textCC.getText()));
		Button buttonDesvincular = pc.getButton(buttonStack, m.textDesvincular,
				e -> actionPessoa.desvincularConfirmar(textCC.getText()));

		pc.toTopControl(buttonVincular);

		createStackPessoa(parent);

		actionPessoa.addListener(this, ActionPessoa.CHANGE_PART, e -> {
			switch (e.getValue(0, EnumPart.class)) {

			case CHANGE_PART_TITLE:
				String title = e.getValue(1, String.class);
				part.setLabel(title);
				break;
			case QUESTION_DESVINCULAR:
				String message = e.getValue(1, String.class);
				boolean res = MessageDialog.openQuestion(shell, "", message);
				if (res) {
					actionPessoa.desvincular(textCC.getText());
				}
				break;
			case LOAD_VINCULO:
				sync.asyncExec(() -> {
					Vinculo vinculo = e.getValue(1, Vinculo.class);
					pc.observeTextString(textMatricula, "matricula", vinculo);
					pc.toTopControl(vinculo == null ? buttonVincular : buttonDesvincular);
				});
				break;
			case ENABLE_VINCULO:
				boolean enabled = e.getValue(1, boolean.class);
				pc.enabled(enabled, buttonVincular, buttonDesvincular);
				break;
			case LOAD_PESSOA_PJ:
				sync.asyncExec(() -> {
					PessoaJuridica pj = e.getValue(1, PessoaJuridica.class);
					pc.observeTextString(textCC, "cnpj", pj);
				});
				break;
			case LOAD_PESSOA_PF:

				break;
			case WAITING:

				Cursor cursor = new Cursor(display, e.getValue(1, boolean.class) ? SWT.CURSOR_WAIT : SWT.CURSOR_ARROW);
				shell.setCursor(cursor);

				String text = e.getValue(2, String.class);
				status.setLeftMessage(text);

				break;
			case PREPARE_TO_LOAD_PESSOA:
				switch (e.getValue(1, int.class)) {
				case Pessoa.NONE:
					pc.enabled(true, buttonAvancarSemCC);
					pc.toTopControl(buttonAvancarSemCC);
					break;
				default:
					pc.enabled(true, buttonAvancar);
					pc.toTopControl(buttonAvancar);
					pc.setDefaultButton(buttonAvancar);
					break;
				}
				break;
			case SHOW_ERROR_DIALOG:
				sync.asyncExec(() -> {
					String msg = e.getValue(1, String.class);
					MessageDialog.openError(shell, m.titleRegistrationPeoples, msg);
				});
				break;
			case RESET:
				textCC.setText("");
				pc.enabled(true, textCC, labelCC);
				pc.enabled(false, buttonAvancar, labelMudarPessoa, comboMudarPessoa, buttonConfirmar);
				pc.toTopControl(buttonAvancarSemCC);
				sync.asyncExec(() -> textCC.setFocus());
				break;
			case AVANCAR_SEM_CC:
				pc.enabled(true, comboMudarPessoa, labelMudarPessoa);
				comboMudarPessoa.setFocus();
			case AVANCAR:
				pc.enabled(false, textCC, labelCC);
				pc.enabled(true, buttonCancelar);
				sync.asyncExec(() -> pc.toTopControl(buttonCancelar));
				break;
			case SET_PESSOA:
				Integer pessoa = e.getValue(1, int.class);
				comboViewer.setSelection(new StructuredSelection(pessoa));
				break;
			case ENABLE_CONFIRM_NO_CC:
				Boolean enable = e.getValue(1, boolean.class);
				pc.enabled(enable, buttonConfirmar);
				break;
			case LOAD_TYPE_PESSOAS:
				comboViewer.setInput(e.getListValue(1, Integer.class));
				break;
			default:
				break;
			}
		});

//		actionPessoa.addListener

//		actionPessoa.addListener(this, ActionPessoa.RESET_PART, e->{
//		});

	}

	private void createStackPessoa(Composite parent) {
		Group group = pc.getGroup(parent);
		group.setLayout(new GridLayout(1, false));

		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		group.setLayoutData(gd);

		Composite stack = pc.getStackComposite(group);
		Composite stackPJ = createStackPJ(stack);
		Composite stackPF = createStackPF(stack);
		Composite stackNone = createStackNone(stack);

		actionPessoa.addListener(this, ActionPessoa.CHANGE_PART, e -> {
			switch (e.getValue(0, EnumPart.class)) {
			case PART_TO_PESSOA:
				switch (e.getValue(1, int.class)) {
				case Pessoa.FISICA:
					pc.toTopControl(stackPF);
					break;
				case Pessoa.JURIDICA:
					pc.toTopControl(stackPJ);
					break;
				case Pessoa.NONE:
					pc.toTopControl(stackNone);
					break;
				}
				break;
			default:
				break;
			}
		});

	}

	private Composite createStackPJ(Composite parent_) {
		Composite parent = pc.getComposite(parent_);

		Composite line = pc.getComposite(parent);
		line.setLayout(new GridLayout(4, false));

		Composite c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelNome = pc.getLabel(c, m.labelName);
		Text textNome = pc.getText(c, m.empty);
		pc.setGridDataWidthHint(textNome, 350);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelFanasia = pc.getLabel(c, m.labelFantasia);
		Text textFantasia = pc.getText(c, m.empty);
		pc.setGridDataWidthHint(textFantasia, 300);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelSituacao = pc.getLabel(c, m.labelSituacao);
		Text textSituacao = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textSituacao, 100);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelNaturezaJuridica = pc.getLabel(c, m.labelNaturezaJuridica);
		Text textNaturezaJuridica = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textNaturezaJuridica, 230);

//		TreeViewer treeQSA = new TreeViewer(parent);
		GridData gd = new GridData();
//		gd.widthHint = 180;
//		gd.heightHint = 200;
//		gd.horizontalSpan = 2;
//		
//		treeQSA.getTree().setLayoutData(gd);
//		

		line = pc.getComposite(parent);
		line.setLayout(new GridLayout(4, false));
		gd = new GridData(GridData.FILL_HORIZONTAL);
		line.setLayoutData(gd);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelTipo = pc.getLabel(c, m.labelTipo);
		Text textTipo = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textTipo, 100);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelAbertura = pc.getLabel(c, m.labelDataAbertura);
		Text textAbertura = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textAbertura, 130);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelCapitalSocial = pc.getLabel(c, m.labelCapitalSocial);
		Text textCapitalSocial = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textCapitalSocial, 140);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label labelAtividadePrincipal = pc.getLabel(c, m.labelAtividadePrincipal);
		Text textAtividadePrincipal = pc.getText(c, m.empty);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		textAtividadePrincipal.setLayoutData(gd);

		line = pc.getComposite(parent);
		line.setLayout(new GridLayout(6, false));
		line.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		c = pc.getComposite(line);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pc.clearMargins(c);
		pc.getLabel(c, m.labelLogradouro);
		Text textLogradouro = pc.getText(c, m.empty);
		textLogradouro.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		c = pc.getComposite(line);
		pc.clearMargins(c);
		pc.getLabel(c, m.labelNumero);
		Text textNumero = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textNumero, 70);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		pc.getLabel(c, m.labelBairro);
		Text textBairro = pc.getText(c, m.empty);
		pc.setGridDataWidthHint(textBairro, 150);

		c = pc.getComposite(line);
		c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pc.clearMargins(c);
		pc.getLabel(c, m.labelMunicipio);
		Text textMunicipio = pc.getText(c, m.empty);
		textMunicipio.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		c = pc.getComposite(line);
		pc.clearMargins(c);
		pc.getLabel(c, m.labelUf);
		Text textUf = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textUf, 40);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		pc.getLabel(c, m.labelCep);
		Text textCep = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		pc.setGridDataWidthHint(textCep, 100);

		line = pc.getComposite(parent);
		line.setLayout(new GridLayout(2, false));
		line.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		c = pc.getComposite(line);
		pc.clearMargins(c);
		c.setLayoutData(new GridData(GridData.FILL_BOTH));
		Label labelAtividadesSecundarias = pc.getLabel(c, m.labelAtividadesSecundarias);
		TableViewer tableViewer = new TableViewer(c, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		gd = new GridData(GridData.FILL_BOTH);

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLayoutData(gd);

		pc.createCollumn(tableViewer, SWT.CENTER, m.textCode, 100);
		pc.createCollumn(tableViewer, SWT.NONE, m.textDescricao, 400);

		c = pc.getComposite(line);
		pc.clearMargins(c);
		Label labelQSA = pc.getLabel(c, m.labelQuadroSocios);

		TreeViewer treeQSA = new TreeViewer(c);
		gd = new GridData();
		gd.widthHint = 250;
		gd.heightHint = 200;

		treeQSA.getTree().setLayoutData(gd);

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

		treeQSA.getTree().setHeaderVisible(true);
		treeQSA.getTree().setLinesVisible(true);

		actionPessoa.addListener(this, ActionPessoa.CHANGE_PART, e -> {
			switch (e.getValue(0, EnumPart.class)) {

			case LOAD_PESSOA_PJ:
				sync.asyncExec(() -> {
					PessoaJuridica pj = e.getValue(1, PessoaJuridica.class);
					pc.observeTextString(textNome, "nome", pj);
					pc.observeTextString(textFantasia, "fantasia", pj);
					pc.observeTextString(textSituacao, "situacao", pj);
					pc.observeTextString(textAbertura, "abertura", pj);
					pc.observeTextString(textCapitalSocial, "scapitalSocial", pj);
					pc.observeTextString(textAtividadePrincipal, "satividadePrincipal", pj);
					pc.observeTextString(textNaturezaJuridica, "naturezaJuridica", pj);
					pc.observeTextString(textTipo, "tipo", pj);

					pc.observeTextString(textLogradouro, "logradouro", pj);
					pc.observeTextString(textNumero, "numero", pj);
					pc.observeTextString(textBairro, "bairro", pj);
					pc.observeTextString(textMunicipio, "municipio", pj);
					pc.observeTextString(textUf, "uf", pj);
					pc.observeTextString(textCep, "cep", pj);

					if (pj == null) {
						sync.asyncExec(() -> treeQSA.setInput(null));
					}
					pc.registerViewerSupport(PessoaJuridica.class, tableViewer, Arrays.asList(), "code", "text");

					if (pj != null) {
						pc.registerViewerSupport(PessoaJuridica.class, tableViewer, pj.getAtividadesSecundarias(),
								"code", "text");
					}
				});
				break;
			case LOAD_QSA:
				@SuppressWarnings("unchecked")
				Map<String, QualGrupo> mapSocios = (Map<String, QualGrupo>) e.getValue(1);
				sync.asyncExec(() -> treeQSA.setInput(mapSocios));
				break;
			case LOCK_FOR_EDITION:
				boolean lock = !e.getValue(1, boolean.class);
				pc.enabled(lock, labelFanasia, labelNome, labelSituacao, labelNaturezaJuridica, labelTipo,
						labelAbertura, labelQSA, labelCapitalSocial, labelAtividadePrincipal,
						labelAtividadesSecundarias);
				pc.editable(lock, textFantasia, textNome, textSituacao, textNaturezaJuridica, textTipo, textAbertura,
						textCapitalSocial, textAtividadePrincipal);
				break;
			case EDIT_PJ:
				sync.asyncExec(() -> textNome.setFocus());
				break;
			default:
				break;
			}
		});

		return parent;
	}

	private Composite createStackPF(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		Label labelNome = pc.getLabel(parent, m.labelNome);
		Text textNome = pc.getText(parent, m.empty);
		pc.setGridDataWidthHint(textNome, 340);

		actionPessoa.addListener(this, ActionPessoa.CHANGE_PART, e -> {
			switch (e.getValue(0, EnumPart.class)) {
			case LOCK_FOR_EDITION:
				boolean lock = !e.getValue(1, boolean.class);
				pc.enabled(lock, labelNome);
				pc.editable(lock, textNome);

				break;
			case EDIT_PF:
				sync.asyncExec(() -> textNome.setFocus());
				break;
			default:
				break;
			}
		});

		return parent;
	}

	private Composite createStackNone(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		pc.getLabel(parent, "None");
		return parent;
	}

	@PreDestroy
	public void preDestroy() {
		actionPessoa.close();
		actionPessoa.removeListeners(this);
		actionSearch.removeListeners(this);
	}

}
