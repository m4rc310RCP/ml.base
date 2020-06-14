package com.m4rc310.ml.parts.pessoa;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionPessoa2;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.PessoaJuridica;
import com.m4rc310.ml.models.QualGrupo;
import com.m4rc310.ml.models.Socio;
import com.m4rc310.ml.ui.parts.PartControl;

@Creatable
public class CompositePJ implements IComposite {
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
//		Group group = pc.getGroup(parent);

		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout());
		parent.setBackground(pc.getColor(SWT.COLOR_GRAY));

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
		table.setLinesVisible(true);
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
		

		actionPessoa.addListener(this, ActionPessoa2.LOAD_PJ, e -> {
			sync.asyncExec(() -> {
				PessoaJuridica pj = e.getValue(PessoaJuridica.class);
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
				
				
				
				pc.registerViewerSupport(PessoaJuridica.class, tableViewer, Arrays.asList(), "code", "text");

				sync.asyncExec(() -> treeQSA.setInput(null));

				if (pj != null) {
					sync.asyncExec(() -> treeQSA.setInput(pj.getMapSocios()));
					pc.registerViewerSupport(PessoaJuridica.class, tableViewer, pj.getAtividadesSecundarias(),
							"code", "text");
				}
				
			});
		});

		actionPessoa.addListener(this, ActionPessoa2.PJ_LOCK_EDIT, e -> {
			boolean lock = !e.getValue(boolean.class);
			pc.editable(lock, textNome, textFantasia, textSituacao, textNaturezaJuridica, textTipo, textAbertura,
					textCapitalSocial, textAtividadePrincipal, textLogradouro, textNumero, textBairro, textMunicipio,
					textUf, textCep);
		});

		return parent;
	}

}
