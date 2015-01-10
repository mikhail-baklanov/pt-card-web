package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;

public class LogPanel extends FlexTable {

	private PtStyles styles = PtResourceBundle.I.styles();

	public LogPanel() {
		setWidth("100%");
		Label label = new Label("Журнал неподтвержденных событий");
		label.setStyleName(styles.columnHeader());
		setWidget(0, 0, label);
		FlexCellFormatter cellFormatter = this.getFlexCellFormatter();
		cellFormatter.setColSpan(0, 0, 3);
		cellFormatter
				.setVerticalAlignment(1, 0, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter
				.setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter
				.setVerticalAlignment(1, 2, HasVerticalAlignment.ALIGN_TOP);
		setHTML(1, 0, "ФИО");
		setHTML(1, 1, "Время");
		setHTML(1, 2, "Статус/Кнопки");
		Timer timer = new Timer() {

			@Override
			public void run() {
				update();
			}
		};
		timer.scheduleRepeating(1000);
	}

	private void update() {
		RestProvider provider = new RestProvider(
				RestProvider.REST_URL + "/passway/entrance");
		provider.getData(new AsyncCallback<JSONObject>() {

			@Override
			public void onSuccess(JSONObject result) {
				JSONArray passes = result.get("passes_response").isObject()
						.get("passes").isArray();
				for (int i = 0; i < passes.size(); i++) {
					JSONObject user = passes.get(i).isObject();
					setHTML(i + 2, 0, user.get("firstName").toString() + " "
							+ user.get("middleName").toString() + " "
							+ user.get("lastName").toString());
					double time = user.get("passTime").isNumber().doubleValue();
					Date date = new Date((new Double(time)).longValue());
					setHTML(i + 2, 1, date.toString());
					setHTML(i + 2, 2, user.get("status").toString());
				}

			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}
}
