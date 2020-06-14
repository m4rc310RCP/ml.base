package com.m4rc310.rcp.ui.utils.search;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IStylingEngine;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.widgets.Text;

import com.m4rc310.rcp.ui.utils.MAction;
import com.m4rc310.rcp.ui.utils.streaming.MEvent;

@Creatable
//@Singleton
public abstract class SearchAction extends MAction implements SearchListener {

	public static final String SHOW_DIALOG_SEARCH = "show_dialog_search";
	public static final String SEARCH_FOR = "search_for";
	public static final String LOAD_SEARCH_RESULT = "load_search_result";
	public static final String CHANGEL_LABEL_TITLE_DIALOG = "changel_label_title_dialog";
	public static final String MAKE_COLUMNS = "make_columns";
	public static final String RETURN_RESULT = "return_result";
	
	public Object value;
	
	
	
	@Inject IEclipseContext context;
	
	@Inject
	IStylingEngine engine;
	
	@Inject
	UISynchronize sync;
	

	@Inject
	public SearchAction() {
	}
	
	public void search(String text) {
		search(text, this);
	}
	
	public abstract void searchUnique(String sid, Text text);
	
	
	public void prepareTextComponent(Text text) {
		engine.setClassname(text, "MSearchComponent");
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				Text txt = (Text)e.widget;
				searchUnique(txt.getText(), txt);
			}
		});

	}
	
	public void showSearchDialog(String label) {
		fire(SHOW_DIALOG_SEARCH, label);
	}

	public void showSearchDialog() {
		showSearchDialog("----");
	}
	
	public void setValue(Object value) {
		this.value = value;
	}
	
	public void labelForSearchDialog(String text) {
		fire(CHANGEL_LABEL_TITLE_DIALOG, text);
	}

	public void fire(String ref, Object... args) {
		sync.asyncExec(() -> {
			stream.fireListener(MEvent.event(this, ref, args));
		});
	}
	
	public void setSearchResult(Object value) {
		fire(LOAD_SEARCH_RESULT, value);
	}

	public void returnResult() {
		fire(RETURN_RESULT, value);
	}

	@Override
	public void initListeners() {
		
	}

	@Override
	public void search(String text, SearchAction action) {
	}

	@Override
	public void makeColumns(TableViewer viewer) {
	}
	
}
