/**
 * Copyright 2000-2012 InterTrust LTD.
 *
 * All rights reserved.
 *
 * Visit our web-site: www.intertrust.ru.
 */
package ru.relex.webClient.client;

import com.google.gwt.resources.client.CssResource;

/**
 * Стили комбобокса.
 * 
 * @author Mikhail Baklanov
 */
public interface PtStyles extends CssResource {

	/**
	 * Стиль ушедшего по рабочим делам.
	 * 
	 * @return имя стиля
	 */
	String workAbsent();

	/**
	 * Стиль ушедшего по личным делам.
	 * 
	 * @return имя стиля
	 */
	String personalAbsent();

	String absentWidget();

	String absentWidgetPhoto();

	String absentWidgetText();

}
