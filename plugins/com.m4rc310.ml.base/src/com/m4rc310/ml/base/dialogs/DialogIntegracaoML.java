package com.m4rc310.ml.base.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.base.actions.ActionML;
import com.m4rc310.ml.base.i18n.M;
import com.m4rc310.ml.base.models.Acesso;
import com.m4rc310.rcp.ui.utils.PartControl;

public class DialogIntegracaoML extends Dialog {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	ActionML action;

	@Inject
	UISynchronize sync;

	@Inject
	Shell shell;

	@Inject
	public DialogIntegracaoML(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("MLS");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group groupMain = pc.getGroup(parent);
		groupMain.setLayout(new GridLayout());

		Composite stack = pc.getComposite(groupMain);
		pc.stackLayout(stack);

		
		Composite p1 = pc.getComposite(stack);
		p1.setLayout(new GridLayout(2, true));
		p1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button buttonInformAccessCode = pc.getButton(p1, "Informar Codigo de Acesso", e -> {
			action.chooseOptionToInformationAccessCode();
		});

		buttonInformAccessCode.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Button buttonGetTestPeriod = pc.getButton(p1, "Solicitar Periodo de Testes", e -> {
			action.chooseGetTestPeriod();
		});

		buttonGetTestPeriod.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite p2 = pc.getComposite(stack);
		p2.setLayout(new GridLayout(3, false));
		p2.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

		pc.getLabel(p2, m.labelCodigo);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 110;

		Text textCodigo = pc.getText(p2, "", SWT.CENTER | SWT.BORDER);
		textCodigo.setLayoutData(gd);

		pc.addTextModifyListener(textCodigo, e -> {
			Text t = (Text) e.widget;
			action.writingCode(t.getText());
		});

		Button buttonAdvance = pc.getButton(p2, m.textAvancar, e -> {
			action.validatingCode();
		});

		Composite p3 = pc.getComposite(stack);
		p3.setLayout(new GridLayout(3, false));
		p3.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

		pc.getLabel(p3, m.labelEmail);
		Text textEmail = pc.getText(p3, "");
		textEmail.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		pc.addTextModifyListener(textEmail, e -> {
			Text t = (Text) e.widget;
			action.writingEmail(t.getText());
		});

		Button buttonGetPeriodTestFronEmail = pc.getButton(p3, m.textSolicitar, e->{
			action.requestAvaluationPeriod();
		});

		Composite p4 = pc.getComposite(stack);
		p4.setLayout(new GridLayout(1, false));
		p4.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
//		Label iconConnectionInvaliable = pc.getLabel(p4, "");
//		Image icon = pc.createImageDescriptor("com.m4rc310.ml.base", "icons/dialog_warning.png").createImage();
//		iconConnectionInvaliable.setImage(icon);
//		iconConnectionInvaliable.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));

		Button buttonTryAgain = pc.getButton(p4, m.textTryAgain, e->{
			action.init();
		});
		
		buttonTryAgain.setLayoutData(new GridData(GridData.CENTER, GridData.CENTER, true, true));
		
//		Composite p5 = pc.getComposite(stack);
//		p5.setLayout(new GridLayout(2, false));
//		
//		
//		pc.getLabel(p5, m.labelEmail);
//		Text textEmail2 = pc.getText(p5, m.empty);
//		textEmail2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		
//		pc.getLabel(p5, m.labelName);
//		Text textName = pc.getText(p5, m.empty);
//		textName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
//		Button buttonFinality = pc.getButton(p5, m.textFinality);
//		gd = new GridData();
//		gd.horizontalSpan = 3;
//		buttonFinality.setLayoutData(gd);

		Composite infoBar = pc.getComposite(groupMain);
		infoBar.setLayout(new GridLayout(2, false));
		infoBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label labelIcon = pc.getLabel(infoBar, "0");
		
		gd = new GridData();
		gd.widthHint = 16;
		gd.heightHint = 16;

		labelIcon.setLayoutData(gd);

		Label labelInforme = pc.getLabel(infoBar, "0");

		pc.clearMargins(stack, p1, groupMain, p4,p3, p2, infoBar);

		action.addListener(this, ActionML.PRE_VALIDATE_CODE, e -> {
			boolean valid = e.getValue(boolean.class);
			if (valid) {
				p2.getShell().setDefaultButton(valid ? buttonAdvance : null);
			}
			pc.enabled(valid, buttonAdvance);
		});

		action.addListener(this, ActionML.SHOW_MESSAGE, e -> {
			String pathIcon = e.getValue(0, String.class);
			String msg = e.getValue(1, String.class);
			ImageDescriptor img = pc.createImageDescriptor("com.m4rc310.ml.base", pathIcon);
			labelIcon.setImage(img.createImage());
			labelInforme.setText(msg);
			infoBar.layout();
		});
		
		

		action.addListener(this, ActionML.REQUESTING_AVALUATION_PERIOD, e -> {
			boolean requesting = e.getValue(boolean.class);
			pc.enabled(!requesting, textEmail, buttonGetPeriodTestFronEmail);
		});
		
		action.addListener(this, ActionML.VALIDATING_CODE, e -> {
			boolean validating = e.getValue(boolean.class);
			pc.enabled(!validating, buttonAdvance, textCodigo);
		});
		
		action.addListener(this, ActionML.DISPOSE, e->{
			close();
		});

		action.addListener(this, ActionML.TO_CHOOSE, e -> {
			int option = e.getValue(0, int.class);
			switch (option) {
			case ActionML.REGISTER:
//				pc.toTopControl(p5);
				
				break;
			case ActionML.LOAD_ACESSO:
				Acesso acesso = e.getValue(1, Acesso.class);
				pc.observeTextString(textEmail, "email", acesso);
			case ActionML.VALID_CODE:
				boolean valid = e.getValue(1, boolean.class);
				if(!valid) {
					textCodigo.setFocus();
					textCodigo.selectAll();
				}
				break;
			case ActionML.VALID_EMAIL:
				valid = e.getValue(1, boolean.class);
				pc.enabled(valid, buttonGetPeriodTestFronEmail);
				if(valid) {
					p3.getShell().setDefaultButton(buttonGetPeriodTestFronEmail);
				}
				break;
			case ActionML.LOCK_DIALOG:
				pc.toTopControl(p4);
//				pc.enabled(false, buttonGetPeriodTestFronEmail, buttonGetTestPeriod);
				break;
			case ActionML.INIT_ACTIONS:
				pc.toTopControl(p1);
				pc.enabled(false, getButton(IDialogConstants.BACK_ID));
				break;
			case ActionML.GET_PERIOD_TESTS:
				pc.toTopControl(p3);
				pc.enabled(true, getButton(IDialogConstants.BACK_ID), textEmail);
				textEmail.setText("");
				textEmail.setFocus();
				break;
			case ActionML.VALIDATE_CODE:
				pc.toTopControl(p2);
				pc.enabled(true, textCodigo);
				textCodigo.setFocus();
				textCodigo.setText("");
				pc.enabled(true, getButton(IDialogConstants.BACK_ID));
				break;
			default:
				pc.toTopControl(p1);
				break;
			}
		});

		
		action.init();
		
		
		return parent;
	}

//	private void toTopControl(Control control) {
//		try {
//			Layout layout = control.getParent().getLayout();
//			if(layout instanceof StackLayout) {
//				((StackLayout)layout).topControl=control;
//				control.getParent().layout();
//			}
//			
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.BACK_ID) {
			action.init();
			return;
		}

		super.buttonPressed(buttonId);
	}

	@Override
	public boolean close() {
		action.removeListeners(this);
		return super.close();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout());
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pc.clearMargins(parent);

		pc.getComposite(parent).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = createButton(parent, IDialogConstants.BACK_ID, m.textVoltar, false);
		button.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, m.textSair, false);
	}

}
