package ru.relex.webClient.client.rest;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.RequestPermissionException;
import com.google.gwt.xhr.client.ReadyStateChangeHandler;
import com.google.gwt.xhr.client.XMLHttpRequest;

public class SystemRequestBuilder extends RequestBuilder {

	// public final static String H_AUTH = "Authorization";
	public final static String H_CONTENT_TYPE = "Content-Type";
	// public final static String H_ACCEPT_LANGUAGE = "Accept-Language";

	/**
	 * Map of header name to value that will be added to the JavaScript
	 * XmlHttpRequest object before sending a request.
	 */
	private Map<String, String> headers = new HashMap<String, String>();

	private static Map<String, String> commonHeaders = new HashMap<String, String>();

	/**
	 * Creates a builder using the parameters values for configuration.
	 * 
	 * @param httpMethod
	 *            HTTP method to use for the request
	 * @param url
	 *            URL that has already has already been URL encoded. Please see
	 *            {@link com.google.gwt.http.client.URL#encode(String)} and
	 *            {@link com.google.gwt.http.client.URL#encodePathSegment(String)}
	 *            and
	 *            {@link com.google.gwt.http.client.URL#encodeQueryString(String)}
	 *            for how to do this.
	 * @throws IllegalArgumentException
	 *             if the httpMethod or URL are empty
	 * @throws NullPointerException
	 *             if the httpMethod or the URL are null
	 */
	SystemRequestBuilder(String httpMethod, String url) {

		super(httpMethod, url);

		// add http header Accept-Language (server-side localization)
		// http://www.w3.org/Protocols/rfc2616/rfc2616-sec14.html#sec14.4
		// setHeader(H_ACCEPT_LANGUAGE, "ru;q=1");

		// add common headers
		for (String key : commonHeaders.keySet()) {
			setHeader(key, commonHeaders.get(key));
		}
	}

	/**
	 * Creates a builder using the parameters values for configuration.
	 * 
	 * @param httpMethod
	 *            HTTP method to use for the request
	 * @param url
	 *            URL that has already has already been URL encoded. Please see
	 *            {@link com.google.gwt.http.client.URL#encode(String)} and
	 *            {@link com.google.gwt.http.client.URL#encodePathSegment(String)}
	 *            and
	 *            {@link com.google.gwt.http.client.URL#encodeQueryString(String)}
	 *            for how to do this.
	 * @throws IllegalArgumentException
	 *             if the httpMethod or URL are empty
	 * @throws NullPointerException
	 *             if the httpMethod or the URL are null
	 */
	public SystemRequestBuilder(Method httpMethod, String rtURL) {
		this(httpMethod.toString(), rtURL);
	}

	/**
	 * Returns the value of a header previous set by
	 * {@link #setHeader(String, String)}, or <code>null</code> if no such
	 * header was set.
	 * 
	 * @param header
	 *            the name of the header
	 */
	@Override
	public String getHeader(String header) {
		if (headers == null) {
			return null;
		}
		return headers.get(header);
	}

	/**
	 * Sets a request header with the given name and value. If a header with the
	 * specified name has already been set then the new value overwrites the
	 * current value.
	 * 
	 * @param header
	 *            the name of the header
	 * @param value
	 *            the value of the header
	 * @throws NullPointerException
	 *             if header or value are null
	 * @throws IllegalArgumentException
	 *             if header or value are the empty string
	 */
	@Override
	public void setHeader(String header, String value) {
		throwIfEmptyOrNull("header", header);
		throwIfEmptyOrNull("value", value);
		if (headers == null) {
			headers = new HashMap<String, String>();
		}
		headers.put(header, value);
	}

	/**
	 * Add common header for all requests
	 * 
	 * @param header
	 * @param value
	 * @throws NullPointerException
	 *             if header or value are null
	 * @throws IllegalArgumentException
	 *             if header or value are the empty string
	 */
	public static void addCommonHeaders(String header, String value) {
		throwIfEmptyOrNull("header", header);
		throwIfEmptyOrNull("value", value);
		commonHeaders.put(header, value);
	}

	/**
	 * Sends an HTTP request based on the current builder configuration. If no
	 * request headers have been set, the header "Content-Type" will be used
	 * with a value of "text/plain; charset=utf-8". You must call
	 * {@link #setRequestData(String)} and {@link #setCallback(RequestCallback)}
	 * before calling this method.
	 * 
	 * @return a {@link Request} object that can be used to track the request
	 * @throws RequestException
	 *             if the call fails to initiate
	 * @throws NullPointerException
	 *             if a request callback has not been set
	 */
	@Override
	public Request send() throws RequestException {
		throwIfNull("callback", getCallback());
		return doSend(getRequestData(), getCallback());
	}

	/**
	 * Sends an HTTP request based on the current builder configuration with the
	 * specified data and callback. If no request headers have been set, the
	 * header "Content-Type" will be used with a value of
	 * "text/plain; charset=utf-8". This method does not cache
	 * <code>requestData</code> or <code>callback</code>.
	 * 
	 * @param requestData
	 *            the data to send as part of the request
	 * @param callback
	 *            the response handler to be notified when the request fails or
	 *            completes
	 * @return a {@link Request} object that can be used to track the request
	 * @throws NullPointerException
	 *             if <code>callback</code> <code>null</code>
	 */
	@Override
	public Request sendRequest(String requestData, RequestCallback callback)
			throws RequestException {
		throwIfNull("callback", callback);
		return doSend(requestData, callback);
	}

	/**
	 * Sends an HTTP request based on the current builder configuration. If no
	 * request headers have been set, the header "Content-Type" will be used
	 * with a value of "text/plain; charset=utf-8".
	 * 
	 * @return a {@link Request} object that can be used to track the request
	 * @throws RequestException
	 *             if the call fails to initiate
	 * @throws NullPointerException
	 *             if request data has not been set
	 * @throws NullPointerException
	 *             if a request callback has not been set
	 */
	private Request doSend(String requestData, final RequestCallback callback)
			throws RequestException {
		XMLHttpRequest xmlHttpRequest = null;
		try {
			xmlHttpRequest = XMLHttpRequestFactory.create(getHTTPMethod(),
					getUrl());
		} catch (JavaScriptException e) {
			RequestPermissionException requestPermissionException = new RequestPermissionException(
					getUrl());
			requestPermissionException.initCause(new RequestException(e
					.getMessage()));
			throw requestPermissionException;
		}

		setHeaders(xmlHttpRequest);

		final SystemRequest request = new SystemRequest(xmlHttpRequest,
				getTimeoutMillis(), callback);

		// Must set the onreadystatechange handler before calling send().
		xmlHttpRequest.setOnReadyStateChange(new ReadyStateChangeHandler() {
			@Override
			public void onReadyStateChange(XMLHttpRequest xhr) {
				if (xhr.getReadyState() == XMLHttpRequest.DONE) {
					xhr.clearOnReadyStateChange();
					request.fireOnResponseReceived(callback);
				}
			}
		});

		try {
			xmlHttpRequest.send(requestData);
		} catch (JavaScriptException e) {
			throw new RequestException(e.getMessage());
		}

		return request;
	}

	/**
	 * Throws a {@link NullPointerException} if the value is <code>null</code>.
	 * 
	 * @param name
	 *            the name of the value, used in error messages
	 * @param value
	 *            the value that needs to be validated
	 * @throws NullPointerException
	 *             if the value is <code>null</code>
	 */
	public static void throwIfNull(String name, Object value) {
		if (null == value) {
			throw new NullPointerException(name + " cannot be null");
		}
	}

	/**
	 * Throws if <code>value</code> is <code>null</code> or empty. This method
	 * ignores leading and trailing whitespace.
	 * 
	 * @param name
	 *            the name of the value, used in error messages
	 * @param value
	 *            the string value that needs to be validated
	 * @throws IllegalArgumentException
	 *             if the string is empty, or all whitespace
	 * @throws NullPointerException
	 *             if the string is <code>null</code>
	 */
	public static void throwIfEmptyOrNull(String name, String value) {
		assert name != null;
		assert name.trim().length() != 0;
		throwIfNull(name, value);
		if (0 == value.trim().length()) {
			throw new IllegalArgumentException(name + " cannot be empty");
		}
	}

	/*
	 * Internal method that actually sets our cached headers on the underlying
	 * JavaScript XmlHttpRequest object. If there are no headers set, then we
	 * set the "Content-Type" to "text/plain; charset=utf-8". This is really
	 * lining us up for integration with RPC.
	 */
	private void setHeaders(XMLHttpRequest xmlHttpRequest)
			throws RequestException {
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> header : headers.entrySet()) {
				try {
					xmlHttpRequest.setRequestHeader(header.getKey(),
							header.getValue());
				} catch (JavaScriptException e) {
					throw new RequestException(e.getMessage());
				}
			}

			// validate headers
			// if (headers.containsKey(H_CONTENT_TYPE)) {
			// // validateHeaders();
			// }
		} else {

		}
		xmlHttpRequest.setRequestHeader(H_CONTENT_TYPE, "application/json");
	}

	// private void validateHeaders() {
	// String hval = headers.get(H_CONTENT_TYPE);
	// if (hval.indexOf("charset") != -1) {
	// String hvres = cutString(hval, "charset", "\"");
	// headers.put(H_CONTENT_TYPE, hvres);
	// }
	// }

	public static Map<String, String> getCommonHeaders() {
		return commonHeaders;
	}

	private String cutString(String original, String cuttable, String delimiter) {

		StringBuilder sb = new StringBuilder();

		int indSt = original.indexOf(cuttable);
		int indEn = original.indexOf(delimiter, indSt + 1);

		if (-1 == indSt) {
			return original;
		} else if (-1 == indEn) {
			return original.substring(0, indSt);
		}

		sb.append(original.substring(0, indSt));
		sb.append(original.substring(indEn + 1).trim());

		return sb.toString().trim();
	}

}
