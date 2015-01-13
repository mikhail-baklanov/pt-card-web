package ru.relex.webClient.client;

import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;

/**
 * Панель для отображения фотографии.
 * 
 * @since 4.3
 * @author Lapin Denis
 */
public class FotoPanel extends AbsolutePanel {

  private static final int TWO = 2;
  private Image            userPhoto;

  /**
   * Пустой конструктор.
   */
  public FotoPanel() {
    super();

  }

  /**
   * Построение панели с фотографией.
   * 
   * @param fotoUrl
   *          ссылка на фото
   * @param width
   *          ширина панели
   * @param height
   *          высота панели
   */
  public void buildContent(String fotoUrl, final int width, final int height) {
    this.setSize(width + "px", height + "px");
    if (fotoUrl != null) {
      userPhoto = new Image(fotoUrl);
    }
    else {
      userPhoto = new Image("");
    }

    userPhoto.addLoadHandler(new LoadHandler() {
      @Override
      public void onLoad(final LoadEvent arg0) {
        composeUserPhoto(height, width);
      }
    });
    userPhoto.getElement().getStyle().setVisibility(Visibility.HIDDEN);
    this.add(userPhoto);
  }

  /**
   * Масштабирование фотографии под размеры панели.
   * 
   * @param panelHeight
   *          высота панели
   * @param panelWidth
   *          ширина панели
   */
  private void composeUserPhoto(int panelHeight, int panelWidth) {
    int width = userPhoto.getWidth();
    int height = userPhoto.getHeight();
    float kh = 0, kw = 0;
    int left = 0, top = 0;

    if (height < panelHeight) {
      top = (panelHeight - height) / TWO;
    }
    else {
      kh = (float) height / panelHeight;
    }
    if (width < panelWidth) {
      left = (panelWidth - width) / TWO;
    }
    else {
      kw = (float) width / panelWidth;
    }
    if (kh != 0 || kw != 0) {
      if (kh >= kw) {
        userPhoto.setHeight(panelHeight + "px");
        left = (panelHeight - (int) (width / kh)) / TWO;
        userPhoto.setWidth(panelHeight - TWO * left + "px");
      }
      else {
        userPhoto.setWidth(panelWidth + "px");
        top = (panelWidth - (int) (height / kw)) / TWO;
        userPhoto.setHeight(panelWidth - TWO * top + "px");
      }
    }

    this.clear();
    this.add(userPhoto, left, top);
    userPhoto.getElement().getStyle().setVisibility(Visibility.VISIBLE);
  }

  /**
   * Добавление обработчика события нажатия мыши.
   * 
   * @param mouseDownHandler
   *          обработчик события нажатия мыши
   */
  public void addMouseStickerHndl(MouseDownHandler mouseDownHandler) {
    userPhoto.addHandler(mouseDownHandler, MouseDownEvent.getType());
  }

  /**
   * Добавление обработчика события начала прикосновения.
   * 
   * @param touchStartHandler
   *          обработчик события начала прикосновения
   */
  public void addTouchStickerHndl(TouchStartHandler touchStartHandler) {
    userPhoto.addHandler(touchStartHandler, TouchStartEvent.getType());
  }
}