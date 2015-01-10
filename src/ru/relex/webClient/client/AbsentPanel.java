package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.model.AbsentInfo;
import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AbsentPanel extends VerticalPanel {

	private PtStyles styles = PtResourceBundle.I.styles();

	public AbsentPanel() {
		setWidth("100%");
		setHeight("100%");
		Label label = new Label("Отошли");
		label.setStyleName(styles.columnHeader());
		add(label);
		addAbsent();
	}

	private void addAbsent() {
		AbsentInfo absentInfo = new AbsentInfo();
		absentInfo.setPassTime(new Date());
		absentInfo.setAbsentTimeMin(45);
		absentInfo.setFirstName("Иван");
		absentInfo.setLastName("Петров");

		AbsentWidget absentWidget = new AbsentWidget(absentInfo,
				RestProvider.REST_URL);
		add(absentWidget);
	}

	private void updateAbsentList() {
	}
}
