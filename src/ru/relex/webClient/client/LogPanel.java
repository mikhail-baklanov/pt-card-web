package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class LogPanel extends FlexTable {

//	private PtStyles styles = PtResourceBundle.I.styles();
	private int linesCount = 0;

	public LogPanel() {
		FlexCellFormatter cellFormatter = this.getFlexCellFormatter();
		cellFormatter
				.setVerticalAlignment(linesCount, 0, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter
				.setVerticalAlignment(linesCount, 1, HasVerticalAlignment.ALIGN_TOP);
		cellFormatter
				.setVerticalAlignment(linesCount, 2, HasVerticalAlignment.ALIGN_TOP);
		setHTML(linesCount, 0, "ФИО");
		setHTML(linesCount, 1, "Время");
		setHTML(linesCount, 2, "Статус/Кнопки");
		linesCount++;
		Timer timer = new Timer() {

			@Override
			public void run() {
				update();
			}
		};
		//timer.scheduleRepeating(3000);
		update();
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
					setHTML(linesCount, 0, user.get("firstName").toString() + " "
							+ user.get("middleName").toString() + " "
							+ user.get("lastName").toString());
					double time = user.get("passTime").isNumber().doubleValue();
					Date date = new Date((new Double(time)).longValue());
					setHTML(linesCount, 1, date.toString());
					setHTML(linesCount, 2, user.get("status").toString());
					linesCount++;
				}

			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}
}
