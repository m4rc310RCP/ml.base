package com.m4rc310.ml.base.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.base.actions.ActionCadastroML;
import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Usuario;
import com.m4rc310.rcp.ui.utils.PartControl;

public class DialogIniciarIntegracaoML extends Dialog {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	ActionCadastroML action;
	
	@Inject
	IStylingEngine style;

	@Inject
	UISynchronize sync;

	@Inject
	public DialogIniciarIntegracaoML(Shell shell) {
		super(shell);
	}

	@Override
	protected Control createDialogArea(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group g1 = pc.getGroup(parent);
		g1.setLayout(new GridLayout(1, false));
		g1.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite p1 = pc.getComposite(g1);
		p1.setLayout(new GridLayout(1, false));
		p1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite p1Top = pc.getComposite(p1);
		p1Top.setLayout(new GridLayout(3, false));
		p1Top.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		
		
		pc.getLabel(p1Top, m.labelCodigo);
		Text textCodigo = pc.getText(p1Top, "", SWT.BORDER | SWT.CENTER);
		pc.addTextModifyListener(textCodigo, e -> {
			Text widget = (Text) e.widget;
			action.digitandoCodigo(widget.getText());
		});
		
//		style.setClassname(p1, "MTest");

		GridData gd = new GridData();
		gd.widthHint = 120;
		textCodigo.setLayoutData(gd);

		Button buttonAvancar = pc.getButton(p1Top, m.textAvancar, e -> {
			action.validarCodigo();
		});

		Button buttonAvaliacao = pc.getButton(p1Top, m.textSolicitarPeriodoAvaliacao, e->{
			action.prepararSolicitacao();
		});

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		buttonAvaliacao.setLayoutData(gd);

		Composite p2 = pc.getComposite(g1);
		p2.setLayout(new GridLayout(3, false));
		p2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		pc.getLabel(p2, m.labelEmail);
		Text textEmail = pc.getText(p2, "");
		pc.addTextModifyListener(textEmail, e->{
			Text t = (Text)e.widget;
			action.digitandoEmail(t.getText());
		});
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=240;
		textEmail.setLayoutData(gd);
		
		Button buttonSolicitar = pc.getButton(p2, m.textSolicitar, e->{
			action.solicitarPeriodoAvaliacao();
		});
		
		
//		textEmail.set
		
		Composite p3 = pc.getComposite(g1);
		p3.setLayout(new GridLayout(1, false));
		p3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		Button buttonIntegracao = pc.getButton(p3, m.textConectarMercadoLivre);
		buttonIntegracao.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		
		Link linkAlternativo = new Link(p3, SWT.NONE);
		linkAlternativo.setText(m.linkAlternativoMl);
		linkAlternativo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		
		Composite p4 = pc.getComposite(g1);
		p4.setLayout(new GridLayout(1, false));
		p4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		
		pc.getLabel(p4, m.labelUrlMercadoLivre);
		Text textUrlML = pc.getText(p4,	"");
		textUrlML.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		style.setClassname(p3, "MTest");
		
		
		Composite infoParent = pc.getComposite(g1);
		infoParent.setLayout(new GridLayout(2, false));
		infoParent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		infoParent.setLayoutData(gd);

		pc.clearMargins(infoParent, p1Top, p1,p2,p3,p4);

		Label labelIcon = pc.getLabel(infoParent, "");
		gd = new GridData();
		gd.widthHint = 16;
		gd.heightHint = 16;
		labelIcon.setLayoutData(gd);

		Label labelMensagem = pc.getLabel(infoParent, "");
//		gd = new GridData();
//		gd.widthHint = 230;
//		labelMensagem.setLayoutData(gd);
		
		action.addListener(this, ActionCadastroML.LOAD_USUARIO, e->{
			Usuario usuario = e.getValue(Usuario.class);
			pc.observeTextString(textEmail, "email", usuario);
		});
		

		action.addListener(this, ActionCadastroML.INFORME_MESSAGE, e -> {
			int tipo = e.getValue(0, int.class);

			String pathIcon;

			switch (tipo) {
			case ActionCadastroML.MESSAGEM_ERRO:
				pathIcon = "icons/exclamation-2.png";
				break;
			case ActionCadastroML.MESSAGEM_INFO:
				pathIcon = "icons/ok.png";
				break;
			case ActionCadastroML.MESSAGEM_WAIT:
				pathIcon = "icons/wait.png";
				break;
			case ActionCadastroML.MESSAGEM_EXCLAMATION:
			default:
				pathIcon = "icons/exclamation.png";

			}

			ImageDescriptor img = pc.createImageDescriptor("com.m4rc310.ml.base", pathIcon);
			labelIcon.setImage(img.createImage());

			String text = e.getValue(1, String.class);
			labelMensagem.setText(text);
			infoParent.layout();
		});

		action.addListener(this, ActionCadastroML.HABILITAR_BOTAO_SOLICITAR, e->{
			Boolean enabled = e.getValue(boolean.class);
			pc.enabled(enabled, buttonSolicitar);
			getShell().setDefaultButton(enabled ? buttonSolicitar : null);
		});
		
		action.addListener(this, ActionCadastroML.HABILITAR_BOTAO_AVANCAR, e -> {
			Boolean enabled = e.getValue(boolean.class);
			pc.enabled(enabled, buttonAvancar);
			pc.enabled(!enabled, buttonAvaliacao);
			getShell().setDefaultButton(enabled ? buttonAvancar : null);
			textCodigo.setFocus();
		});

		action.addListener(this, ActionCadastroML.VERIFICANDO_CODIGO_ACESSO, e -> {
			Boolean enabled = e.getValue(boolean.class);
			pc.enabled(!enabled, buttonAvancar, textCodigo);
			if (!enabled) {
				textCodigo.setFocus();
				textCodigo.selectAll();
			}
		});
		
		action.addListener(this, ActionCadastroML.PREPARAR_DIGITACAO_EMAIL, e->{
			textEmail.setFocus();
			textEmail.selectAll();
		});
		
		action.addListener(this, ActionCadastroML.SOLICITANDO_PERIODO_AVALIACAO, e->{
			Boolean solicitando = e.getValue(boolean.class);
			pc.enabled(!solicitando, textEmail, buttonSolicitar);
		});

		action.init();

		return parent;
	}

	@Override
	public boolean close() {
		action.getStream().removeListenerFromTarget(this);
		return super.close();
	}

}
