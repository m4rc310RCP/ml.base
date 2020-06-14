package com.m4rc310.rcp.popup.notifications;

import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Link;

//import org.eclipse.mylyn.internal.provisional.commons.ui.AbstractNotificationPopup;

//@SuppressWarnings("unused")
public class NotificationPopUpUI extends MNotificationPopup {

	private String title;
	private String text;

	public NotificationPopUpUI(Display display, String title, String text) {
		this(display, getDefaultImage(), title, text);
	}

	public NotificationPopUpUI(Display display, String text) {
		this(display, getDefaultImage(), "@Message", text);
	}

	public NotificationPopUpUI(Display display, Image popupImage, String title, String text) {
		super(display);
		setDefaultImage(popupImage);
		this.title = title;
		this.text = text;
	}

	@Override
	protected void createContentArea(Composite composite) {
		composite.setLayout(new GridLayout(1, true));
		Link linkGoogleNews = new Link(composite, 0);
//		String googlenewsLink = "This is a link to <a href=\"https://news.google.com\">Google News</a>";
		String googlenewsLink = text;
		linkGoogleNews.setText(googlenewsLink);
		linkGoogleNews.setSize(500, 150);

		linkGoogleNews.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {

//				Browser browser = new Browser(composite, SWT.None);
//				WebViewer

//				WebViewer.display(path, WebViewer.HTML, browser,"frameset");

//				try {
//					PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser().openURL(new URL(e.text));
//				} catch (PartInitException e1) {
//					e1.printStackTrace();
//				} catch (MalformedURLException e1) {
//					e1.printStackTrace();
//				}
			}
		});
	}

//	@Override
//	protected String getPopupShellTitle() {
//		return "It's News Time!";
//	}

	@Override
	protected Image getPopupShellImage(int maximumHeight) {
		return getDefaultImage();
	}

	@Override
	protected String getPopupShellTitle() {
		return title;
	}
}
