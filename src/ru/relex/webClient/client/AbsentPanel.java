package ru.relex.webClient.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ru.relex.webClient.client.model.AbsentInfo;
import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;

public class AbsentPanel extends FlowPanel {

	// private PtStyles styles = PtResourceBundle.I.styles();
	private Map<Integer, AbsentInfo> users = new HashMap<Integer, AbsentInfo>();

	public AbsentPanel() {
		Timer timer = new Timer() {

			@Override
			public void run() {
				updateAbsentList();
			}
		};
		timer.scheduleRepeating(5000);
	}

	private void addAbsent() {
		AbsentInfo absentInfo = new AbsentInfo();
		absentInfo.setPassTime(new Date());
		absentInfo.setAbsentTimeMin(45);
		absentInfo.setFirstName("Иван");
		absentInfo.setLastName("Петров");
		absentInfo.setUserId(2);

		AbsentWidget absentWidget = new AbsentWidget(absentInfo);
		add(absentWidget);
	}

	private void updateAbsentList() {
		RestProvider provider = new RestProvider(
				"http://localhost:8080/pt-api-0.0.3-SNAPSHOT/rest"
						+ "/passway/absent");
		provider.getData(new AsyncCallback<JSONValue>() {

			@Override
			public void onSuccess(JSONValue result) {
				if (result != null && result.isArray() != null) {
					int size = result.isArray().size();
					for (int i = 0; i < size; i++) {
						JSONObject obj = result.isArray().get(i).isObject();
						JSONObject user = obj.get("absentUserInfo").isObject();
						AbsentInfo absentInfo = readAbsentList(user);
						if (absentInfo != null) {
							AbsentWidget absentWidget = new AbsentWidget(
									absentInfo);
							add(absentWidget);
						}
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}
		});
	}

	private AbsentInfo readAbsentList(JSONObject user) {
		AbsentInfo absentInfo = new AbsentInfo();
		absentInfo.setFirstName(user.get("firstName").isString().stringValue());
		absentInfo.setMiddleName(user.get("middleName").isString()
				.stringValue());
		absentInfo.setLastName(user.get("lastName").isString().stringValue());
		absentInfo.setAbsentTimeMin((int) user.get("absentTime").isNumber()
				.doubleValue());
		int userId = (int) user.get("userId").isNumber().doubleValue();
		absentInfo.setUserId(userId);
		Date date = new Date((long) user.get("passTime").isNumber()
				.doubleValue());
		absentInfo.setPassTime(date);
		if (!users.containsKey(userId)) {
			users.put(userId, absentInfo);
			return absentInfo;
		} else
			return null;
	}
}
