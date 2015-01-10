package ru.relex.webClient.client;

import ru.relex.webClient.client.model.PassInfo;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;

public class AcceptPanel extends FlexTable {
  private PtStyles styles     = PtResourceBundle.I.styles();
  private int      linesCount = 0;

  public AcceptPanel() {
    styles.ensureInjected();
    RowFormatter rowFormatter = getRowFormatter();
    rowFormatter.setStyleName(linesCount, styles.secondHeaderRow());

    FlexCellFormatter cellFormatter = this.getFlexCellFormatter();
    cellFormatter.setVerticalAlignment(linesCount, 0, HasVerticalAlignment.ALIGN_TOP);
    cellFormatter.setVerticalAlignment(linesCount, 1, HasVerticalAlignment.ALIGN_TOP);
    cellFormatter.setVerticalAlignment(linesCount, 2, HasVerticalAlignment.ALIGN_TOP);
    cellFormatter.setStyleName(linesCount, 0, styles.secondHeader());
    cellFormatter.setStyleName(linesCount, 1, styles.secondHeader());
    cellFormatter.setStyleName(linesCount, 2, styles.secondHeader());
    cellFormatter.setWidth(linesCount, 0, "40%");
    cellFormatter.setWidth(linesCount, 1, "20%");
    cellFormatter.setWidth(linesCount, 2, "40%");
    setHTML(linesCount, 0, "ФИО");
    setHTML(linesCount, 1, "Время");
    setHTML(linesCount, 2, "Статус");
    linesCount++;
  }

  public void addData(PassInfo passInfo) {
    setHTML(linesCount, 0, passInfo.getFirstName() + " " + passInfo.getMiddleName() + " "
        + passInfo.getLastName());
    setHTML(linesCount, 1, passInfo.getPassTime().toLocaleString());
    setHTML(linesCount, 2, passInfo.getStatus().getName());
    linesCount++;
  }
}
