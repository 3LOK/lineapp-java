package com.paypal;

import java.io.IOException;

import org.battlehack.lineapp.json.Json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;

public class PaypalClient {
	private final HttpRequestFactory requestFactory;
	private final Integer connectTimeout;
	private final Integer readTimeout;
	private final Endpoint endpoint;
	private final Credentials credentials;
	
	public static enum Endpoint {
		SANDBOX("https://svcs.sandbox.paypal.com/AdaptivePayments/");
		
		public String url;
		
		private Endpoint(String url) {
			this.url = url;
		}
	}
	
	public PaypalClient(HttpRequestFactory requestFactory, Integer connectTimeout, Integer readTimeout,
			Endpoint endpoint, Credentials credentials) {
		this.requestFactory = requestFactory;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.endpoint = endpoint;
		this.credentials = credentials;
	}
	
	public PayResponse createPayment(PayRequest payRequest) throws IOException {
		final HttpRequest request = requestFactory.buildPostRequest(
				new GenericUrl(endpoint.url + "Pay"),
				new ByteArrayContent("application/json", Json.bytify(payRequest)));
		if (connectTimeout != null) {
			request.setConnectTimeout(connectTimeout);
		}
		if (readTimeout != null) {
			request.setReadTimeout(readTimeout);
		}
		
		request.getHeaders().setAccept("application/json");
		request.getHeaders().set("X-PAYPAL-APPLICATION-ID", credentials.appId);
		request.getHeaders().set("X-PAYPAL-SECURITY-USERID", credentials.userId);
		request.getHeaders().set("X-PAYPAL-SECURITY-PASSWORD", credentials.password);
		request.getHeaders().set("X-PAYPAL-SECURITY-SIGNATURE", credentials.signature);
		request.getHeaders().set("X-PAYPAL-REQUEST-DATA-FORMAT", "JSON");
		request.getHeaders().set("X-PAYPAL-RESPONSE-DATA-FORMAT", "JSON");
		
		final HttpResponse response = request.execute();
		try {
			return Json.parse(response.getContent(), new TypeReference<PayResponse>() {});
		} finally {
			response.ignore();
		}
	}
	
	public PayDetailsResponse getPaymentDetails(PayDetailsRequest payRequest) throws IOException {
		final HttpRequest request = requestFactory.buildPostRequest(
				new GenericUrl(endpoint.url + "PaymentDetails"),
				new ByteArrayContent("application/json", Json.bytify(payRequest)));
		if (connectTimeout != null) {
			request.setConnectTimeout(connectTimeout);
		}
		if (readTimeout != null) {
			request.setReadTimeout(readTimeout);
		}
		
		request.getHeaders().setAccept("application/json");
		request.getHeaders().set("X-PAYPAL-APPLICATION-ID", credentials.appId);
		request.getHeaders().set("X-PAYPAL-SECURITY-USERID", credentials.userId);
		request.getHeaders().set("X-PAYPAL-SECURITY-PASSWORD", credentials.password);
		request.getHeaders().set("X-PAYPAL-SECURITY-SIGNATURE", credentials.signature);
		request.getHeaders().set("X-PAYPAL-REQUEST-DATA-FORMAT", "JSON");
		request.getHeaders().set("X-PAYPAL-RESPONSE-DATA-FORMAT", "JSON");
		
		final HttpResponse response = request.execute();
		try {
//			final BufferedReader br = new BufferedReader(new InputStreamReader(response.getContent(), "UTF-8"));
//			System.out.println(br.readLine());
//			return null;
			return Json.parse(response.getContent(), new TypeReference<PayDetailsResponse>() {});
		} finally {
			response.ignore();
		}
	}
}
