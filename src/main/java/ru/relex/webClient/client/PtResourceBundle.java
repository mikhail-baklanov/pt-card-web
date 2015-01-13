/**
 * Copyright 2000-2012 InterTrust LTD.
 *
 * All rights reserved.
 *
 * Visit our web-site: www.intertrust.ru.
 */
package ru.relex.webClient.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;

/**
 * Стили.
 * 
 * @author Mikhail Baklanov
 */
public interface PtResourceBundle extends ClientBundle {

  PtResourceBundle I = GWT.create(PtResourceBundle.class);

  /**
   * Ресурсы.
   * 
   * @return стили
   */
  @Source("pt.css")
  PtStyles styles();

}
