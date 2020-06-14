package com.m4rc310.ml.actions;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.nls.Translation;
import org.eclipse.jface.viewers.TableViewer;

import com.m4rc310.ml.dialogs.DialogSearch;
import com.m4rc310.ml.i18n.M;
import com.m4rc310.ml.ui.actions.MAction;

@Creatable
@Singleton
public class ActionSearch extends MAction {
	public static final String SEARCH_ACTION = "search_action";
	public static final String RETURN_VALUE = "return_value";

	public enum EnumSearch {
		CONFIGURE_TABLE_VIEW, PROCESS_SEARCH, RESULT_OF_SEARCH, RESULT_SELECTED, RETURN_VALUE
	}

	@Inject
	@Translation
	M m;

	@Inject
	IEclipseContext context;

	public void showSearchDialog() {
		DialogSearch dialogSearch = ContextInjectionFactory.make(DialogSearch.class, context);
		dialogSearch.open();
	}

	public void configureTableResult(TableViewer tableViewer) {
		fire(SEARCH_ACTION, EnumSearch.CONFIGURE_TABLE_VIEW, tableViewer);
	}
	
	public void search(String text, boolean phonetic) {
		fire(SEARCH_ACTION, EnumSearch.PROCESS_SEARCH, text, phonetic, this);
	}

}
