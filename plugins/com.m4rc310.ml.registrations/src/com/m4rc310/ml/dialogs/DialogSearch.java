package com.m4rc310.ml.dialogs;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.ml.actions.ActionSearch;
import com.m4rc310.ml.actions.ActionSearch.EnumSearch;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.ui.parts.PartControl;

public class DialogSearch extends Dialog {

	@Inject
	PartControl pc;

	@Inject
	@Translation
	M m;

	@Inject
	ActionSearch search;

	@Inject
	UISynchronize sync;

	private Text textPesquisa;

	private Button checkPesquisaFonetica;

	private TableViewer tableViewer;

	@Inject
	public DialogSearch(IShellProvider parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent_) {
		parent_.setLayout(new GridLayout(1, false));

		Composite parent = pc.getGroup(parent_);
		parent.setLayout(new GridLayout(1, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 600;
		gd.heightHint = 250;

//		parent.setLayoutData(gd);

		Composite linha = pc.getComposite(parent);
		linha.setLayout(new GridLayout(2, false));
		linha.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label labelPesquisa = pc.getLabel(linha, m.labelPesquisarPor);
		gd = new GridData();
		gd.horizontalSpan = 2;
		labelPesquisa.setLayoutData(gd);

		this.textPesquisa = pc.getText(linha, m.empty);
		textPesquisa.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//		pc.setGridDataWidthHint(textPesquisa, 360);

		this.checkPesquisaFonetica = pc.getButton(linha, m.textPesquisaFonetica, SWT.CHECK);
		checkPesquisaFonetica.setSelection(true);
		pc.enabled(false, checkPesquisaFonetica);

		linha = pc.getComposite(parent);
		linha.setLayout(new GridLayout(1, false));
		linha.setLayoutData(new GridData());

		this.tableViewer = new TableViewer(linha, SWT.H_SCROLL | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 180;
//		gd.widthHint=500;

		Table table = tableViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		table.setLayoutData(gd);

//		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {
//
//			@Override
//			public void selectionChanged(SelectionChangedEvent event) {
//				IStructuredSelection ss = ((TableViewer) event.getSource()).getStructuredSelection();
//
////				Button buttonOk = getButton(IDialogConstants.OK_ID);
////				Button buttonSearch = getButton(IDialogConstants.DETAILS_ID);
//////
////				boolean isEmpty = ss.isEmpty();
//////
//////				pc.enabled(false, buttonSearch);
////				pc.enabled(!isEmpty, buttonOk);
////				pc.setDefaultButton(isEmpty?buttonSearch:buttonOk);
//
////				search.fire(ActionSearch.SEARCH_ACTION, EnumSearch.RESULT_SELECTED, ss.getFirstElement());
//			}
//		});

		table.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {

				Button buttonSearch = getButton(IDialogConstants.DETAILS_ID);
				Button buttonOk = getButton(IDialogConstants.OK_ID);

				pc.enabled(false, buttonSearch);
				pc.enabled(true, buttonOk);
				pc.setDefaultButton(buttonOk);
			}
		});

		textPesquisa.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Button buttonSearch = getButton(IDialogConstants.DETAILS_ID);
				Button buttonOk = getButton(IDialogConstants.OK_ID);
				pc.enabled(true, buttonSearch);
				pc.enabled(false, buttonOk);
				pc.setDefaultButton(buttonSearch);
			}
		});

		textPesquisa.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					if (table.getItemCount() > 0) {
						table.setFocus();
						table.setSelection(0);
//						Object selected = table.getItem(0)
					}
				}
			}
		});

		search.configureTableResult(tableViewer);

		search.addListener(this, ActionSearch.SEARCH_ACTION, e -> {
			switch (e.getValue(0, EnumSearch.class)) {
			case RESULT_OF_SEARCH:
				List<Object> listResult = e.getListValue(1, Object.class);
				tableViewer.setInput(listResult);
				break;
			default:
				break;
			}
		});

		return parent;
	}

	@Override
	public boolean close() {
		search.removeListeners(this);

		return super.close();
	}

	@Override
	protected void buttonPressed(int buttonId) {
		switch (buttonId) {
		case IDialogConstants.DETAILS_ID:
			search.search(textPesquisa.getText(), checkPesquisaFonetica.getSelection());
			break;
		case IDialogConstants.OK_ID:
			IStructuredSelection ss = tableViewer.getStructuredSelection();
			Object selected = ss.getFirstElement();
			search.fire(ActionSearch.RETURN_VALUE, selected);
			close();
			break;
		}
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayout(new GridLayout());
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		pc.clearMargins(parent);

		pc.getComposite(parent).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

//		Composite stack = pc.getStackComposite(parent);

//		Button button = createButton(parent, IDialogConstants.CANCEL_ID, m.textCancel, false);
		Button button = createButton(parent, IDialogConstants.OK_ID, m.textOk, false);
		button.setEnabled(false);
		createButton(parent, IDialogConstants.DETAILS_ID, m.textSearch, true);

//		pc.toTopControl(buttonSearch);

	}

}
