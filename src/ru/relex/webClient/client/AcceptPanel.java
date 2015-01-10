package ru.relex.webClient.client;

import ru.relex.webClient.client.model.PassInfo;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class AcceptPanel extends FlexTable {
	private int linesCount = 0;

	public AcceptPanel() {
		FlexCellFormatter cellFormatter = this.getFlexCellFormatter();
		cellFormatter.setVerticalAlignment(linesCount, 0,
				HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setVerticalAlignment(linesCount, 1,
				HasVerticalAlignment.ALIGN_TOP);
		cellFormatter.setVerticalAlignment(linesCount, 2,
				HasVerticalAlignment.ALIGN_TOP);
		setHTML(linesCount, 0, "ФИО");
		setHTML(linesCount, 1, "Время");
		setHTML(linesCount, 2, "Статус");
		linesCount++;
	}

	public void addData(PassInfo passInfo) {
		setHTML(linesCount, 0,
				passInfo.getFirstName() + " " + passInfo.getMiddleName() + " "
						+ passInfo.getLastName());
		setHTML(linesCount, 1, passInfo.getPassTime().toLocaleString());
		setHTML(linesCount, 2, passInfo.getStatus().getName());
	}
}
