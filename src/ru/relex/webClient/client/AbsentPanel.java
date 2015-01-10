package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.model.AbsentInfo;

import com.google.gwt.user.client.ui.FlowPanel;

public class AbsentPanel extends FlowPanel {

//	private PtStyles styles = PtResourceBundle.I.styles();

	public AbsentPanel() {
		addAbsent();
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
    absentWidget = new AbsentWidget(absentInfo);
    add(absentWidget);
    absentWidget = new AbsentWidget(absentInfo);
    add(absentWidget);
	}

	private void updateAbsentList() {
	}
}
