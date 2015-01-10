package ru.relex.webClient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MainView extends Composite {

	private static MainViewUiBinder uiBinder = GWT
			.create(MainViewUiBinder.class);

	interface MainViewUiBinder extends UiBinder<Widget, MainView> {
	}

	// @UiField
	// AbsentPanel absentPanel;
	//
	// @UiField
	// LogPanel confirmedPanel;
	//
	// @UiField
	// LogPanel dirtyListPanel;

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}
