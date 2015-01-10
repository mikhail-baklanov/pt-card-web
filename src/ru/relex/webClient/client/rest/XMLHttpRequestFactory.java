package ru.relex.webClient.client.rest;

import com.google.gwt.xhr.client.XMLHttpRequest;

public class XMLHttpRequestFactory {

  /**
   * Creates an XMLHttpRequest object. Common headers are NOT included.
   * 
   * @param method
   *          HTTP method
   * @param url
   *          request URL
   * @return the created object
   */
  // @formatter:off
  final static native XMLHttpRequest create(String method, String url) 
  
  /*-{

  // XDomainRequest for IE doesn't support cookies and custom headers
  // So we simply block application when CORS isn't supported
  var xhr = null;

  // detecting IE
  var isIE = navigator.appName == 'Microsoft Internet Explorer';

  // XHR for Chrome/Firefox/Opera/Safari
  if ($wnd.XMLHttpRequest) {
    xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.withCredentials = "true";
  }
  // IE with version 8 and 9 supports XDomainRequest
  else if (isIE && typeof XDomainRequest != "undefined") {
    xhr = new XDomainRequest();
    xhr.open(method, url);
  } else if (isIE) {
    try {
      xhr = new $wnd.ActiveXObject('MSXML2.XMLHTTP.3.0');
      xhr.open(method, url);
    } catch (e) {
      xhr = new $wnd.ActiveXObject("Microsoft.XMLHTTP");
      xhr.open(method, url);
    }
  }

  return xhr;
}-*/;
  
  //@formatter:on

  /**
   * Creates an XMLHttpRequest object. Common headers are included.
   * 
   * @return the created object
   */
  public final static XMLHttpRequest create(HTTPMethod method, String url) {

    XMLHttpRequest req = create(method.toString(), url);

    for (String header : SystemRequestBuilder.getCommonHeaders().keySet()) {
      req.setRequestHeader(header, SystemRequestBuilder.getCommonHeaders().get(header));
    }

    return req;
  }
  
  public enum HTTPMethod {
	  GET,POST,PUT;
  }
}
