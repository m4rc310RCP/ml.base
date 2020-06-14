
package com.m4rc310.ml.parts;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionRegistration;
import com.m4rc310.ml.actions.ActionRegistration.EnumDialogState;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.models.Pessoa;
import com.m4rc310.ml.ui.parts.PartControl;

@Deprecated
public class PartEmployee {

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
	public PartEmployee() {

	}
	
	@PostConstruct
	public void post(Composite parent) {
		parent.setLayout(new GridLayout());
		CTabFolder folder = pc.createCTabFolder(parent);
		folder.setLayoutData(new GridData(GridData.FILL_BOTH));
		folder.setTabPosition(SWT.BOTTOM);
		folder.setBorderVisible(true);

		CTabItem item = pc.createCTabItem(folder, m.textPessonalInformations);
		
		ScrolledComposite sc = pc.getScrolledComposite(folder);
		
		Composite c1 = contentForPersonalInformations(folder);
		
		sc.setContent(c1);
		
		sc.setExpandHorizontal(true);
	    sc.setExpandVertical(true);
	    sc.setMinSize(c1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
	    sc.setShowFocusedControl(true);
	    item.setControl(c1);
		
//		CTabItem item = pc.createCTabItem(folder, m.textPessonalInformations, icon);
		
	}
	
	private Composite contentForPersonalInformations(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout());
		GridData sc = new GridData();
		sc.widthHint = 300;
		sc.heightHint = 400;
		parent.setLayoutData(sc);
		return parent;
	}
	
	
//	private void createTabPersonalInfo(CTabFolder folder) {
//		CTabItem tab = pc.createCTabItem(folder, "t1");
//		
//	}
	

	public void postConstruct(Composite parent_, MPart part) {
		Composite parent = pc.getComposite(parent_);
		ScrolledComposite scroll = pc.getScrolledComposite(parent_);
		parent.setLayout(new GridLayout(2, false));
		
		CTabFolder cTabFolder = pc.createCTabFolder(parent);
		cTabFolder.setBorderVisible(true);
		cTabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		
//		CTabItem cTabItemPersonalInformations = pc.createCTabItem(cTabFolder, m.textPessonalInformations);
		
		GridData gd = new GridData();

		Composite c1 = pc.getComposite(parent);
		c1.setLayout(new GridLayout());
		
		
		createTopIdentify(c1);
//
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

//		pc.clearMargins(c1, c2, c3);

		Point size = parent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		scroll.setMinSize(size);
		scroll.setContent(parent);
		
//		cTabItemPersonalInformations.setControl(parent);

		action.addListener(this, ActionRegistration.CHANGE_TITLE, e -> {
			String title = e.getValue(String.class);
			part.setLabel(title);
		});		
		
		action.init();
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

		pc.getLabel(c, m.labelName);
		Text textName = pc.getText(c, m.empty);
		gd = new GridData();
		gd.widthHint = 130;
		textName.setLayoutData(gd);

		pc.addTextModifyListener(textName, e -> action.writeNome(textName.getText()));

//
//		pc.getLabel(c, m.labelCpf);
//		Text textCPF = pc.getText(c, m.textEmpty, SWT.CENTER | SWT.BORDER);
//		textCPF.setLayoutData(gd);
//
//		pc.getLabel(c, m.labelName);
//		Text textName = pc.getText(c, m.textEmpty);
//		gd = new GridData(GridData.FILL_HORIZONTAL);
//		gd.widthHint = 250;
//		textName.setLayoutData(gd);
//
//		action.addListener(this, ActionRegistration.PREPARE_DIALOG, e -> {
//			switch (e.getValue(EnumDialogState.class)) {
//			case PREPARE_TO_LOAD_CLERK:
//				textName.setFocus();
//				break;
//			default:
//			}
//		});
//
//		action.addListener(this, ActionRegistration.LOAD_CLERK, e -> {
//			Clerk clerk = e.getValue(Clerk.class);
//			pc.observeTextString(textName, "name", clerk);
//		});
	}

	private void createTopIdentify(Composite parent) {

		Composite c = pc.getComposite(parent);
		c.setLayout(new GridLayout(7, false));
		GridData gd = new GridData();
		c.setLayoutData(gd);
		pc.clearMargins(c);

		pc.getLabel(c, m.labelId);
		Text textId = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);
		gd = new GridData();
		gd.widthHint = 40;
		textId.setLayoutData(gd);
		pc.configureVerifyValues("[0-9]", textId);

		pc.addTextModifyListener(textId, e -> {
			Text t = (Text) e.widget;
			action.writeId(t.getText());
		});

		Composite st1 = pc.getStackComposite(c);

		Button buttonAdvance = pc.getButton(st1, m.textAdvance);
		Button buttonSearch = pc.getButton(st1, m.textSearchClerk);
		Button buttonCancel = pc.getButton(st1, m.textCancel, e -> action.cancel());

		pc.getLabel(c, m.labelCpf);
		Text textCPF = pc.getText(c, m.empty, SWT.BORDER | SWT.CENTER);

		pc.addTextModifyListener(textCPF, e -> action.writeCpf(textCPF.getText()));

		gd = new GridData();
		gd.widthHint = 130;
		textCPF.setLayoutData(gd);

		Composite st2 = pc.getStackComposite(c);

		Button checkNoCpf = pc.getButton(st2, m.textNoCpf, SWT.CHECK,
				e -> action.noHasCpf(((Button) e.widget).getSelection()));

		Button buttonVarifyCpf = pc.getButton(st2, m.textOk);
		Button buttonChangeCpf = pc.getButton(st2, m.textChange, e -> action.changeCPF());

		pc.toTopControl(checkNoCpf);

		action.addListener(this, ActionRegistration.ON_FOCUS, e -> textId.setFocus());

		action.addListener(this, ActionRegistration.PREPARE_DIALOG, e -> {
			switch (e.getValue(EnumDialogState.class)) {
			case PREPARE_TO_LOAD_CLERK:
				pc.enabled(false, textId);
				pc.enabled(true, textCPF);
				pc.toTopControl(buttonCancel);
				break;
			case RESET_DIALOG:
				pc.enabled(true, textId);
				textId.setFocus();
				textId.selectAll();
				pc.toTopControl(checkNoCpf);
				pc.enabled(true, textCPF);
				break;
			case CPF_VALID:
				pc.toTopControl(buttonVarifyCpf);
				pc.setDefaultButton(buttonVarifyCpf);
				break;
			case CPF_INVALID:
				pc.toTopControl(checkNoCpf);
				break;
			case CHANGE_CPF:
				sync.asyncExec(() -> {
					pc.enabled(true, textCPF);
					textCPF.selectAll();
					textCPF.setFocus();
				});
			case HAS_CPF:
				sync.asyncExec(() -> {
					pc.enabled(true, textCPF);
					textCPF.setFocus();
				});
				break;
			case NO_CHANGE_CPF:
				pc.toTopControl(buttonChangeCpf);
				pc.enabled(false, textCPF);
				break;
			case NO_HAS_CPF:
				sync.asyncExec(() -> {
					pc.enabled(false, textCPF);
					textCPF.setText("");
				});
				break;
			default:
				break;
			}
		});

		action.addListener(this, ActionRegistration.INFORME_VALID_ID, e -> {
			Boolean isOK = e.getValue(boolean.class);
			pc.toTopControl(isOK ? buttonAdvance : buttonSearch);
			if (isOK) {
				st1.getShell().setDefaultButton(buttonAdvance);
			}
		});

		action.addListener(this, ActionRegistration.LOAD_CLERK, e -> {
			Pessoa clerk = e.getValue(Pessoa.class);
			pc.observeTextString(textId, "id", clerk);
			pc.observeTextString(textCPF, "cpf", clerk);
			pc.observeCheck(checkNoCpf, "noContainCpf", clerk);

		});

		pc.toTopControl(buttonSearch);

	}

	@PreDestroy
	public void preDestroy() {

	}

	@Focus
	public void onFocus() {
	}

	@Persist
	public void save() {

	}

}