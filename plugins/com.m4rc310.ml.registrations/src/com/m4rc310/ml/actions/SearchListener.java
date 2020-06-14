package com.m4rc310.ml.actions;

import java.util.List;

import org.eclipse.jface.viewers.TableViewer;

public interface SearchListener {
	void prepareTableResult(TableViewer tableViewer);
	void processSearch(String text, boolean phonetic, List<Object>result);
	void resultOfSearch(List<Object>results);
}
