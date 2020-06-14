package com.m4rc310.rcp.ui.utils.search;

import org.eclipse.jface.viewers.TableViewer;

public interface SearchListener {
	void initListeners();
	void search(String text, SearchAction action);
	void makeColumns(TableViewer viewer);
}
