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

  private PtStyles                styles       = PtResourceBundle.I.styles();
  private int                     linesCount   = 0;
  private static final int        TIMER_PERIOD = 5000;
  private long                    lastUpdate   = 0;
  private RestProvider            provider     = new RestProvider(RestProvider.REST_URL
                                                   + "/passway/entrance");
  private AsyncCallback<PassInfo> callback;

  public LogPanel() {
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
    setHTML(linesCount, 2, "Действия");
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

  private void fillRow(JSONObject user) {
    PassInfo passInfo = readPassInfo(user);
    RowFormatter rowFormatter = getRowFormatter();
    rowFormatter.setStyleName(linesCount, styles.unacceptableRow());
    setHTML(linesCount, 0, passInfo.getFirstName() + " " + passInfo.getMiddleName() + " "
        + passInfo.getLastName());
    setHTML(linesCount, 1, passInfo.getPassTime().toLocaleString());
    setWidget(linesCount, 2, buildStatusPanel(passInfo, linesCount));
    linesCount++;
  }

  private HorizontalPanel buildStatusPanel(PassInfo passInfo, int rowNum) {
    HorizontalPanel panel = new HorizontalPanel();
    switch (passInfo.getStatus()) {
      case ABSENT:
      case AWAY:
      case NONE:
        Button btnIn = new Button("Пришел");
        setupButton(btnIn, passInfo, "work", rowNum, null);
        Button btnCancel = new Button("Отмена");
        setupButton(btnCancel, passInfo, "ignore", rowNum, null);
        panel.add(btnIn);
        panel.add(btnCancel);
        break;
      case WORK:
        Button btnOut = new Button("Ушел");
        setupButton(btnOut, passInfo, "away", rowNum, null);
        Button btnWork = new Button("По работе");
        setupButton(btnWork, passInfo, "absent", rowNum, "work");
        Button btnPers = new Button("По личным");
        setupButton(btnPers, passInfo, "absent", rowNum, "personal");
        Button btnCancel2 = new Button("Отмена");
        setupButton(btnCancel2, passInfo, "ignore", rowNum, null);
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

  private void setupButton(Button btn, PassInfo passInfo, String status, int rowNum,
      String absentType) {
    btn.addClickHandler(new BtnClickHandler(passInfo, status, rowNum, absentType));
    btn.addStyleName(styles.actionButton());
  }

  private class BtnClickHandler implements ClickHandler {
    PassInfo passInfo;
    String   newStatus;
    int      rowNum = Integer.MAX_VALUE;
    String   absentType;

    public BtnClickHandler(PassInfo passInfo, String newStatus, int rowNum, String absentType) {
      this.rowNum = rowNum;
      this.passInfo = passInfo;
      this.newStatus = newStatus;
      this.absentType = absentType;
    }

    @Override
    public void onClick(ClickEvent event) {

      String request = "{\"process\":{\"id\":"
          + passInfo.getId()
          + ", \"status\":\""
          + newStatus
          + "\""
          + (absentType != null && absentType.trim().length() > 0 ? ",\"absentType\"=\""
              + absentType + "\"" : "") + ", \"absentTimeMin\":30}}";
      provider.putData(request, new AsyncCallback<JSONObject>() {

        @Override
        public void onSuccess(JSONObject result) {
          LogPanel.this.removeRow(rowNum);
          linesCount--;
          if (!"ignore".equals(newStatus)) {
            callback.onSuccess(passInfo);
          }
        }

        @Override
        public void onFailure(Throwable caught) {
          // TODO Auto-generated method stub
        }
      });

    }
  }

  public void addAcceptCallback(AsyncCallback<PassInfo> callback) {
    this.callback = callback;
  }

  private void update() {
    RestProvider provider = new RestProvider(RestProvider.REST_URL + "/passway/entrance?since="
        + lastUpdate);

    provider.getData(new AsyncCallback<JSONObject>() {

      @Override
      public void onSuccess(JSONObject result) {
        JSONObject responce = result.get("passes_response").isObject();
        if (responce != null) {
          long lastUpdateTime = (long) responce.get("lastUpdateTime").isNumber().doubleValue();
          if (lastUpdateTime > 0)
            lastUpdate = lastUpdateTime;
          JSONValue passes = responce.get("passes");
          if (passes.isArray() != null) {
            for (int i = 0; i < passes.isArray().size(); i++) {
              JSONObject user = passes.isArray().get(i).isObject();
              fillRow(user);
            }
          }
          else if (passes.isObject() != null) {
            fillRow(passes.isObject());
          }
        }
      }

      @Override
      public void onFailure(Throwable caught) {

      }
    });
  }

  private PassInfo readPassInfo(JSONObject json) {
    PassInfo passInfo = new PassInfo();
    passInfo.setId((int) json.get("id").isNumber().doubleValue());
    passInfo.setFirstName(json.get("firstName").isString().stringValue());
    passInfo.setLastName(json.get("lastName").isString().stringValue());
    passInfo.setMiddleName(json.get("middleName").isString().stringValue());
    double time = json.get("passTime").isNumber().doubleValue();
    passInfo.setPassTime(new Date((new Double(time)).longValue()));
    UserStatus status = UserStatus.mvalueOf(json.get("status").isString().stringValue());
    passInfo.setStatus(status);
    return passInfo;
  }
}
