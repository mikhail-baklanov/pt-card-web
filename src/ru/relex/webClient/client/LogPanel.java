package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.model.PassInfo;
import ru.relex.webClient.client.model.PassInfo.UserStatus;
import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class LogPanel extends FlexTable {

	// private PtStyles styles = PtResourceBundle.I.styles();
	private int linesCount = 0;
	private static final int TIMER_PERIOD = 5000;
	private long lastUpdate = 0;
	private RestProvider provider = new RestProvider(RestProvider.REST_URL
			+ "/passway/entrance");

	public LogPanel() {
		FlexCellFormatter cellFormatter = this.getFlexCellFormatter();
		cellFormatter.setVerticalAlignment(linesCount, 0,
				HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setVerticalAlignment(linesCount, 1,
				HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setVerticalAlignment(linesCount, 2,
				HasVerticalAlignment.ALIGN_TOP);
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
		timer.scheduleRepeating(TIMER_PERIOD);
		update();
	}

	private void update() {
		RestProvider provider = new RestProvider(RestProvider.REST_URL
				+ "/passway/entrance?since=" + lastUpdate);

		provider.getData(new AsyncCallback<JSONObject>() {

			@Override
			public void onSuccess(JSONObject result) {
				JSONObject responce = result.get("passes_response").isObject();
				if (responce != null) {
					long lastUpdateTime = (long) responce.get("lastUpdateTime")
							.isNumber().doubleValue();
					if (lastUpdateTime > 0)
						lastUpdate = lastUpdateTime;
					JSONValue passes = responce.get("passes");
					if (passes.isArray() != null) {
						for (int i = 0; i < passes.isArray().size(); i++) {
							JSONObject user = passes.isArray().get(i)
									.isObject();
							fillRow(user);
						}
					} else if (passes.isObject() != null) {
						fillRow(passes.isObject());
					}
				}
			}

			@Override
			public void onFailure(Throwable caught) {

			}
		});
	}

	private void fillRow(JSONObject user) {
		PassInfo passInfo = readPassInfo(user);
		setHTML(linesCount, 0,
				passInfo.getFirstName() + " " + passInfo.getMiddleName() + " "
						+ passInfo.getLastName());
		setHTML(linesCount, 1, passInfo.getPassTime().toLocaleString());
		setWidget(linesCount, 2, buildStatusPanel(passInfo));
		linesCount++;
	}

	private PassInfo readPassInfo(JSONObject json) {
		PassInfo passInfo = new PassInfo();
		passInfo.setId((int) json.get("id").isNumber().doubleValue());
		passInfo.setFirstName(json.get("firstName").toString());
		passInfo.setLastName(json.get("lastName").toString());
		passInfo.setMiddleName(json.get("middleName").toString());
		double time = json.get("passTime").isNumber().doubleValue();
		passInfo.setPassTime(new Date((new Double(time)).longValue()));
		UserStatus status = UserStatus.mvalueOf(json.get("status").toString());
		passInfo.setStatus(status);
		return passInfo;
	}

	private HorizontalPanel buildStatusPanel(PassInfo passInfo) {
		HorizontalPanel panel = new HorizontalPanel();
		switch (passInfo.getStatus()) {
		case ABSENT:
		case AWAY:
		case NONE:
			Button btnIn = new Button("Пришел");
			Button btnCancel = new Button("Отмена");
			btnIn.addClickHandler(new BtnClickHandler(passInfo, "work"));
			btnCancel.addClickHandler(new BtnClickHandler(passInfo, "ignore"));
			panel.add(btnIn);
			panel.add(btnCancel);
			break;
		case WORK:
			Button btnOut = new Button("Ушел");
			btnOut.addClickHandler(new BtnClickHandler(passInfo, "away"));
			Button btnWork = new Button("По работе");
			btnWork.addClickHandler(new BtnClickHandler(passInfo, "absent",
					"work"));
			Button btnPers = new Button("По личным");
			btnPers.addClickHandler(new BtnClickHandler(passInfo, "absent",
					"personal"));
			Button btnCancel2 = new Button("Отмена");
			btnCancel2.addClickHandler(new BtnClickHandler(passInfo, "ignore"));
			panel.add(btnOut);
			panel.add(btnWork);
			panel.add(btnPers);
			panel.add(btnCancel2);
			break;
		default:
			break;
		}
		return panel;
	}

	private class BtnClickHandler implements ClickHandler {
		PassInfo passInfo;
		String newStatus;
		String absentType = "";

		public BtnClickHandler(PassInfo passInfo, String newStatus) {
			this.passInfo = passInfo;
			this.newStatus = newStatus;
		}

		public BtnClickHandler(PassInfo passInfo, String newStatus,
				String absentType) {
			this(passInfo, newStatus);
			this.absentType = absentType;
		}

		@Override
		public void onClick(ClickEvent event) {

			String request = "{\"process\":{\"id\":" + passInfo.getId()
					+ ", \"status\":\"" + newStatus + "\"}}";
			// ,\"absentType\"=\"" + absentType + "\"
			provider.putData(request, new AsyncCallback<JSONObject>() {

				@Override
				public void onSuccess(JSONObject result) {
					// TODO Auto-generated method stub
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});

		}
	}

}
