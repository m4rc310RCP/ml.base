
package com.m4rc310.ml.base.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.base.actions.ActionCadastroContaML;
import com.m4rc310.ml.base.actions.ActionCadastroML;
import com.m4rc310.ml.base.actions.ActionML;
import com.m4rc310.ml.base.dialogs.DialogIntegracaoML;
import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.rcp.ui.utils.PartControl;


public class PartMLInfo {

	@Inject
	IStylingEngine style;

	@Inject
	Shell shell;

	@Inject IEclipseContext context;
	
	@Inject
	MPart part;

//	@Inject
//	private SpringBootWebSocketClient webSocket;

	@Inject
	@Translation
	M m;
	
	
//	@Inject ActionCadastroML actionCadastroML;

	@Inject
	public PartMLInfo() {

	}

	@PostConstruct
	public void post(Composite parent_, PartControl pc, ActionML action) {
		Group g1 = pc.getGroup(parent_);
		g1.setLayout(new GridLayout(1, false));
		g1.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite stack = pc.getComposite(g1);
//		pc.stackLayout(stack);
		
		Composite c1 = pc.getComposite(g1);
		c1.setLayout(new GridLayout(1,false));
		c1.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
		Button buttonIniciar = pc.getButton(c1, m.textIniciarIntegracao, e->{
			action.openDialogInit();
		});
		
		buttonIniciar.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		
		pc.getButton(c1, "Test Report", e->{
			action.testReport();
		});
		
		
		pc.toTopControl(c1);
		
		pc.clearMargins(g1,c1,stack);
		
		style.setClassname(c1, "MTest");
		
		action.addListener(this, ActionML.SHOW_REPORT, e->{
			pc.show("com.m4rc310.ml.base.partdescriptor.reports",m.titleReportTest, null);
		});
		
		action.addListener(this, ActionML.OPEN_DIALOG_INTEGRATION, e->{
			ContextInjectionFactory.make(DialogIntegracaoML.class, context).open();
		});
		
	}

	public void post_(Composite parent_, PartControl pc, ActionCadastroML action) {
		ScrolledComposite sc = new ScrolledComposite(parent_, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

		GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		StackLayout stackLayout = new StackLayout();

		Group g1 = pc.getGroup(sc);
		g1.setLayout(new GridLayout(1, false));
		g1.setLayoutData(gd);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setContent(g1);

		Composite stack = pc.getComposite(g1);
		stack.setLayout(stackLayout);

		Composite p1 = pc.getComposite(stack);
		p1.setLayout(new GridLayout(1, false));
		gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		p1.setLayoutData(gd);

		Button buttonSolicitarAvaliacao = pc.getButton(p1, m.textIniciarIntegracao, e -> {
			action.abrirDialogConnect();
//			String dest = "/topics/mls/%s";
//			dest = String.format(dest, "12324");
//			
//			TopicHandler handler = webSocket.subscribe(dest);
//			handler.addListener(message->{
//				System.out.println(message.getContent());
//			});
//			
//			webSocket.connect("ws:localhost:8080/ws/websocket");
//			System.out.println(webSocket.isConnected());
		});

//		Listener's
		
		

		action.addListener(this, ActionCadastroML.INFORME_CONECCAO_SUCESSO, e -> {
			boolean sucesso = e.getValue(boolean.class);
			pc.enabled(sucesso, buttonSolicitarAvaliacao);
		});

		stackLayout.topControl = p1;

	}

	public void postConstruct_(Composite parent_, PartControl pc, ActionCadastroContaML action) {
		ScrolledComposite sc = new ScrolledComposite(parent_, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
//		parent.setLayout(new GridLayout(1, false));

//		actionCadastroML.addListener(this, ActionCadastroML.INFORME_ERROR, e->{
//			part.setLabel(e.getValue(String.class));
//		});

		GridData gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);

		StackLayout stackLayout = new StackLayout();

		Group g1 = pc.getGroup(sc);
		g1.setLayout(new GridLayout(1, false));
		g1.setLayoutData(gd);

		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);

		sc.setContent(g1);

		Composite stack = pc.getComposite(g1);
		stack.setLayout(stackLayout);
//		stack.setLayout(new GridLayout());

		Composite p1 = pc.getComposite(stack);
		p1.setLayout(new GridLayout(1, false));
		gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);

		Composite p1a = pc.getComposite(p1);
		p1a.setLayout(new GridLayout(3, false));
		p1a.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		style.setClassname(p1a, "MTest");

		pc.getLabel(p1a, "Código de Acesso:");

		Text textCodAcesso = pc.getText(p1a, "", SWT.BORDER | SWT.CENTER);
		textCodAcesso.addListener(SWT.Modify, e -> {
			String text = ((Text) e.widget).getText();
			action.digitaCodigo(text);
		});

		gd = new GridData();
		gd.widthHint = 100;
		textCodAcesso.setLayoutData(gd);

		pc.configureNumericValues(textCodAcesso);
//		pc.configureVerifyValues("^\\w{0,5}$", textCodAcesso);

//		^\w{5,10}$

		Button buttonLiberar = pc.getButton(p1a, "Liberar", e -> {
			action.solicitarLiberacao();
		});

		Label labelInfo = pc.getLabel(p1, "Código de acesso inválido!");
		gd = new GridData(GridData.CENTER, GridData.CENTER, true, true);
		gd.horizontalSpan = 3;

		labelInfo.setLayoutData(gd);
		style.setClassname(labelInfo, "MLabelError");

		Composite p2 = pc.getComposite(stack);
		p2.setLayout(new GridLayout(1, false));
		p2.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Button buttonAutorizar = pc.getButton(p2, "Autorizar junto ao Mercado Livre", e -> {
			action.autorizar();
		});
		buttonAutorizar.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Composite p3 = pc.getComposite(stack);
		p3.setLayout(new GridLayout(1, false));
		p3.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Label labelAguardando = pc.getLabel(p3, "Aguardando resposta do Mercado Livre...", SWT.CENTER);
		labelAguardando.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Link link = new Link(p3, SWT.WRAP | SWT.CENTER);

		link.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		link.setText("Clique <a href=\"\">Aqui</a> Caso a página não tenha carregado automáticamente.");
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});

		Composite p4 = pc.getComposite(stack);
		p4.setLayout(new GridLayout(1, false));
		p4.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Label labelBemVindo = pc.getLabel(p4, "Olá FULANO!", SWT.NONE | SWT.CENTER);
		labelBemVindo.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Label labelEmail = pc.getLabel(p4, "fulano@email.com", SWT.NONE | SWT.CENTER);
		labelEmail.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		pc.getButton(p4, "Cancelar", e -> {
			action.cancelar();
		});

		action.putListener(ActionCadastroContaML.ABILITAR_BOTAO_LIBERAR, e -> {
			boolean enabled = e.getValue(boolean.class);
			pc.enabled(enabled, buttonLiberar);
			shell.setDefaultButton(enabled ? buttonLiberar : null);
		});

		action.putListener(ActionCadastroContaML.ABILITAR_BOTAO_AUTORIAR_ML, e -> {
			boolean enabled = e.getValue(boolean.class);
			pc.enabled(enabled, buttonAutorizar);
			shell.setDefaultButton(enabled ? buttonAutorizar : null);
		});

		action.putListener(ActionCadastroContaML.CANCELAR, e -> {
			pc.enabled(true, textCodAcesso);
			textCodAcesso.setText("");
			textCodAcesso.setFocus();
		});

		action.putListener(ActionCadastroContaML.AGUARDE_LIBERACAO, e -> {
			boolean aguarde = !e.getValue(boolean.class);
			pc.enabled(aguarde, buttonLiberar, textCodAcesso);
//			if(!aguarde) {
//				textCodAcesso.setText("");
//				textCodAcesso.setFocus();
//			}
		});

		action.putListener(ActionCadastroContaML.INFORME_ERRO, e -> {
			labelInfo.setText(e.getValue(String.class));
			labelInfo.getParent().layout();
		});

		action.putListener(ActionCadastroContaML.CONTROLE_AMOSTRA, e -> {
			Integer topo = e.getValue(int.class);
			switch (topo) {
			case ActionCadastroContaML.LIBERAR:
				stackLayout.topControl = p1;
				break;
			case ActionCadastroContaML.AUTORIZAR:
				stackLayout.topControl = p2;
				break;
			case ActionCadastroContaML.AGUARDAR:
				stackLayout.topControl = p3;
				break;
			case ActionCadastroContaML.OK:
				stackLayout.topControl = p4;
				break;
			default:
				stackLayout.topControl = p1;
				break;
			}
			stack.layout();
		});

		sc.addListener(SWT.Resize, event -> {
			int width = sc.getClientArea().width;
			sc.setMinSize(sc.computeSize(width, SWT.DEFAULT));
		});

		action.init();
	}

}