package ru.relex.webClient.client.rest;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestBuilder.Method;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RestProvider {
	private final String url;

	public RestProvider(String url) {
		this.url = url;
	}

	public void getData(final AsyncCallback<JSONObject> callback) {
		sendRequest(null, RequestBuilder.GET, callback);
	}

	public void postData(String requestData,
			final AsyncCallback<JSONObject> callback) {
		sendRequest(requestData, RequestBuilder.POST, callback);
	}

	public void putData(String requestData,
			final AsyncCallback<JSONObject> callback) {
		sendRequest(requestData, RequestBuilder.PUT, callback);
	}

	private void sendRequest(String requestData, Method requestMethod,
			final AsyncCallback<JSONObject> callback) {
		SystemRequestBuilder builder = new SystemRequestBuilder(requestMethod,
				url);
		try {
			builder.sendRequest(requestData, new RequestCallback() {

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					callback.onSuccess(JSONParser.parseStrict(
							response.getText()).isObject());
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onFailure(exception);
				}
			});
		} catch (RequestException e) {
			e.printStackTrace();
		}
	}

}
