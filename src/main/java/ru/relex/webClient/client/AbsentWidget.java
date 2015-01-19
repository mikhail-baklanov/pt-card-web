package ru.relex.webClient.client;

import java.util.Date;

import ru.relex.webClient.client.model.AbsentInfo;
import ru.relex.webClient.client.rest.RestProvider;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

public class AbsentWidget extends FlowPanel {
  private PtStyles styles = PtResourceBundle.I.styles();

  public AbsentWidget(AbsentInfo absentInfo) {
    styles.ensureInjected();
    setStyleName(styles.absentWidget());
    FotoPanel photo = new FotoPanel();
    photo.setStyleName(styles.absentWidgetPhoto());
    add(photo);
    photo.buildContent(RestProvider.REST_URL + absentInfo.getAvatarUrl(), 80, 100);
    Label nameLabel = new Label(absentInfo.getLastName() + " " + absentInfo.getFirstName());
    nameLabel.setStyleName(styles.absentWidgetText());
    nameLabel.addStyleName(styles.absentFio());
    add(nameLabel);
    Label periodLabel = new Label(
        getPeriod(absentInfo.getPassTime(), absentInfo.getAbsentTimeMin()));
    periodLabel.setStyleName(styles.absentWidgetText());
    if (AbsentInfo.AbsentType.WORK.equals(absentInfo.getAbsentType())) {
      periodLabel.addStyleName(styles.workAbsent());
    }
    else {
      periodLabel.addStyleName(styles.personalAbsent());
    }
    add(periodLabel);

  }

  public String getPeriod(Date start, int minutes) {

    final long SECONDS = 1000;
    final long MINUTES = 60 * SECONDS;
    Date finish = new Date();
    finish.setTime(start.getTime() + minutes * MINUTES);
    DateTimeFormat df = DateTimeFormat.getFormat("HH:mm");
    return df.format(start) + "-" + df.format(finish);
  }
}
