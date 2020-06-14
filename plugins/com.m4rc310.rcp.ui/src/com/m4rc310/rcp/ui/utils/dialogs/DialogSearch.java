package com.m4rc310.rcp.ui.utils.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.rcp.ui.utils.PartControl;
import com.m4rc310.rcp.ui.utils.i18n.Messages;
import com.m4rc310.rcp.ui.utils.search.SearchAction;

public abstract class DialogSearch extends Dialog {

	@Inject
	PartControl pc;
	@Inject
	@Translation
	Messages m;

	protected SearchAction action;
	
	@Inject
	public DialogSearch(Shell parentShell) {
		super(parentShell);
	}

	public void setAction(SearchAction action) {
		this.action = action;
	}
	
	
	@Override
	protected Control createDialogArea(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(GridData.FILL_BOTH));

		Group ptop = pc.getGroup(parent);
		ptop.setLayout(new GridLayout(1, false));
		ptop.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label labelTitle = pc.getLabel(ptop, m.labelSearch);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);

		Text textSearch = pc.getText(ptop, "", SWT.BORDER | SWT.ICON_SEARCH | SWT.SEARCH | SWT.ICON_CANCEL);
		textSearch.setLayoutData(gd);

		

		textSearch.addListener(SWT.DefaultSelection, e -> {
			Text text = (Text) e.widget;
			action.search(text.getText());
		});

		TableViewer tableViewer = new TableViewer(ptop, SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer.setContentProvider(new ArrayContentProvider());

		textSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.keyCode == SWT.ARROW_DOWN) {
					
					Table table = tableViewer.getTable();
					
					if (table.getItemCount() > 0) {
						table.setFocus();
						table.select(0);
						Object item = table.getItem(0).getData();
						setSelection(item);
					}
				}
			}
		});

//		tableViewer.getTable().addFocusListener(new FocusAdapter() {
//			@Override
//			public void focusGained(FocusEvent e) {
//				
//				
////				IStructuredSelection selection = tableViewer.getStructuredSelection();
////				if (!selection.isEmpty()) {
//					setSelection(((Table)e.widget).getItem(0).getData());
////				}
//			}
//		});
//		
		
		tableViewer.addSelectionChangedListener(event -> {
			IStructuredSelection selection = tableViewer.getStructuredSelection();
			setSelection(selection.getFirstElement());
		});

		Table table = tableViewer.getTable();

		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 95;
		gd.widthHint = 370;

		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		table.setLayoutData(gd);

		action.makeColumns(tableViewer);

//		action.fire(SearchAction.MAKE_COLUMNS, tableViewer);

		action.addListener(this,SearchAction.LOAD_SEARCH_RESULT, e -> {
			tableViewer.setInput(e.getValue(0));
		});
		
		textSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				setSelection(null);
			}
		});
		
		
//		textSearch.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if(action.value!=null) {
//					setSelection(null);
//				}
//			}
//		});

		action.addListener(this,SearchAction.CHANGEL_LABEL_TITLE_DIALOG, e -> {
			labelTitle.setText(e.getValue(String.class));
			labelTitle.getParent().layout();
		});
		
		return parent;
	}
	
	@Override
	public boolean close() {
		action.setValue(null);
		action.removeListeners(this);
		return super.close();
	}

	public void setSelection(Object selected) {
		Button button = getButton(IDialogConstants.OK_ID);
		button.setEnabled(selected != null);
		getShell().setDefaultButton(selected == null ? null : button);
		action.setValue(selected);
	}
	

	@Override
	protected Control createButtonBar(Composite parent_) {
		Composite parent = pc.getComposite(parent_);
		parent.setLayout(new GridLayout(2, false));
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Composite sp = pc.getComposite(parent);
		sp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		createButton(parent, IDialogConstants.CANCEL_ID, m.textCancel, false);
		createButton(parent, IDialogConstants.OK_ID, m.textOk, false);

		setSelection(null);

		return parent;
	}

//	@Override
//	protected Control createButtonBar(Composite parent_) {
//		Composite parent = pc.getComposite(parent_);
//		return parent;
//	}

//	@Override
//	protected void createButtonsForButtonBar(Composite parent) {
//		createButton(parent, CANCEL, m.textCancel, false);
//		createButton(parent, OK, m.textOk, false);
//		super.createButtonsForButtonBar(parent);
//	}

}
