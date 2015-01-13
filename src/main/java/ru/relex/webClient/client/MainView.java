package ru.relex.webClient.client;

import ru.relex.webClient.client.model.PassInfo;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	@UiField
	LogPanel logPanel;

	@UiField
	AcceptPanel accept;

	public MainView() {
		initWidget(uiBinder.createAndBindUi(this));
		logPanel.addAcceptCallback(new AsyncCallback<PassInfo>() {

			@Override
			public void onSuccess(PassInfo result) {
				accept.addData(result);

			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

}
