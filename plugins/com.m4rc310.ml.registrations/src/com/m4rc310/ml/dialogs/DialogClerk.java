package com.m4rc310.ml.dialogs;

import java.awt.image.BufferedImage;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionRegistration;
import com.m4rc310.ml.actions.ActionRegistration.EnumDialogState;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.ui.parts.PartControl;

public class DialogClerk extends Dialog {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	ActionRegistration action;

	@Inject
	UISynchronize sync;

	@Inject
	public DialogClerk(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(2, false));
		
		GridData gd = new GridData();
		
		Composite c1 = pc.getComposite(parent);
		c1.setLayout(new GridLayout());
		createTopIdentify(c1);
		
		gd.horizontalSpan = 2;
		c1.setLayoutData(gd);
		
		Composite c2 = pc.getComposite(parent);
		c2.setLayout(new GridLayout());
		createClerkInfo(c2);
		
		Composite c3 = pc.getComposite(parent);
		c3.setLayout(new GridLayout());
//		gd = new GridData(SWT.CENTER, SWT.CENTER, true, true);
//		c3.setLayoutData(gd);
		
		createCommission(c3);
		
		pc.clearMargins(c1,c2,c3);
		
		return parent;
	}

	private void createCommission(Composite parent) {
		Composite c = pc.getComposite(parent);
		c.setLayout(new GridLayout(1, false));
		pc.getButton(parent, m.textQuestionCommiccion, SWT.CHECK);
		pc.clearMargins(c);
		
	}

	private void createClerkInfo(Composite parent) {
		Composite c = pc.getComposite(parent);
		c.setLayout(new GridLayout(2, false));
		GridData gd = new GridData();
		
		c.setLayoutData(gd);
		pc.clearMargins(c);

		pc.getLabel(c, m.labelCpf);
		Text textCPF = pc.getText(c, m.empty, SWT.CENTER|SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 130;
		textCPF.setLayoutData(gd);
		
		pc.getLabel(c, m.labelName);
		Text textName = pc.getText(c, m.empty);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 250;
		textName.setLayoutData(gd);

		action.addListener(this, ActionRegistration.PREPARE_DIALOG, e -> {
			switch (e.getValue(EnumDialogState.class)) {
			case PREPARE_TO_LOAD_CLERK:
				textName.setFocus();
				break;
			default:
			}
		});
		
		action.addListener(this, ActionRegistration.LOAD_CLERK, e->{
			Pessoa clerk = e.getValue(Pessoa.class);
			pc.observeTextString(textName, "name", clerk);
		});
		
	}

	private void createTopIdentify(Composite parent) {

		Composite c = pc.getComposite(parent);
		c.setLayout(new GridLayout(3, false));
		GridData gd = new GridData();
		c.setLayoutData(gd);
		pc.clearMargins(c);

		pc.getLabel(c, m.labelId);
		Text textId = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		gd = new GridData();
		gd.widthHint = 40;
		textId.setLayoutData(gd);

		pc.addTextModifyListener(textId, e -> {
			Text t = (Text) e.widget;
			action.writeId(t.getText());
		});

		Composite st1 = pc.getStackComposite(c);

//		Button buttonAdvance = pc.getButton(st1, m.textAdvance, e -> action.clerkFronId(textId.getText()));
		Button buttonSearch = pc.getButton(st1, m.textSearchClerk);
		Button buttonCancel = pc.getButton(st1, m.textCancel, e -> action.cancel());

		action.addListener(this, ActionRegistration.PREPARE_DIALOG, e -> {
			switch (e.getValue(EnumDialogState.class)) {
			case PREPARE_TO_LOAD_CLERK:
				pc.enabled(false, textId);
				pc.toTopControl(buttonCancel);
				break;
			case RESET_DIALOG:
				pc.enabled(true, textId);
				textId.setFocus();
				textId.selectAll();
				break;
			default:
				break;
			}
		});

		action.addListener(this, ActionRegistration.INFORME_VALID_ID, e -> {
//			Boolean isOK = e.getValue(boolean.class);
//			pc.toTopControl(isOK ? buttonAdvance : buttonSearch);
//			if (isOK) {
//				st1.getShell().setDefaultButton(buttonAdvance);
//			}
		});
		
		action.addListener(this, ActionRegistration.LOAD_CLERK, e->{
			Pessoa clerk = e.getValue(Pessoa.class);
			pc.observeTextString(textId, "id", clerk);
		});

		pc.toTopControl(buttonSearch);

	}

//	@Override
	protected Control createDialogArea_(Composite parent) {
		parent.setLayout(new GridLayout(1, false));

		Group group = pc.getGroup(parent);
		group.setLayout(new GridLayout(3, false));

		GridData gd = new GridData(GridData.FILL_BOTH);
		group.setLayoutData(gd);

		pc.getLabel(group, m.labelName);

		Text textName = pc.getText(group, "");

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		textName.setLayoutData(gd);
		pc.configureUpperCase(textName);

		Button buttonAdvance = pc.getButton(group, m.textAdvance, e -> {
			action.generateBarcodeClerk(textName.getText());
		});

		pc.addTextModifyListener(textName, e -> {
			String name = textName.getText();
			action.readClerkName(name);
		});

		pc.clearMargins(group);

		Composite barcodeParent = pc.getComposite(group);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		gd.widthHint = 300;
		gd.heightHint = 300;
		barcodeParent.setLayoutData(gd);

		Label labelQrCode = pc.getLabel(barcodeParent, "");
		gd = new GridData(GridData.FILL_BOTH);
		labelQrCode.setLayoutData(gd);

		action.addListener(this, ActionRegistration.INFORME_DATA_OK, e -> {
			boolean ok = e.getValue(boolean.class);
			buttonAdvance.setEnabled(ok);
			if (ok) {
				group.getShell().setDefaultButton(buttonAdvance);
			}
		});

		action.addListener(this, ActionRegistration.LOAD_QRCODE_CLERK_REGISTRATION, e -> {
			BufferedImage img = e.getValue(BufferedImage.class);

			ImageData data = pc.convertToSWT(img);
			Image qrcode = new Image(barcodeParent.getDisplay(), data);

			labelQrCode.setImage(qrcode);
		});

		action.readClerkName("");

		return parent;
	}

	@Override
	public boolean close() {
		action.removeListeners(this);
		action.closeDialog();
		return super.close();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout());
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pc.clearMargins(parent);

		pc.getComposite(parent).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Button button = createButton(parent, IDialogConstants.BACK_ID, m.textComeBack, false);
		button.setEnabled(false);
		createButton(parent, IDialogConstants.CANCEL_ID, m.textExit, false);
	}

}
