package org.battlehack.lineapp.environment;

import com.google.api.client.extensions.appengine.http.UrlFetchTransport;
import com.google.api.client.http.HttpRequestFactory;

/** @see http://en.wikipedia.org/wiki/Singleton_pattern#The_solution_of_Bill_Pugh */
public class Http {
	public static final Integer CONNECT_TIMEOUT = 15000;
	public static final Integer READ_TIMEOUT = 15000;
	
	private Http() {}
	
	/**
     * HttpRequestFactoryHolder is loaded on the first execution of Http.getRequestFactory() 
     * or the first access to HttpRequestFactoryHolder.INSTANCE, not before.
     */
	private static class HttpRequestFactoryHolder {
		public static final HttpRequestFactory INSTANCE = new UrlFetchTransport().createRequestFactory();
	}
	
	private static class HttpRequestFactoryNoSslValidationHolder {
		public static final HttpRequestFactory INSTANCE =
				new UrlFetchTransport.Builder().doNotValidateCertificate().build().createRequestFactory();
	}
	
	public static HttpRequestFactory getRequestFactory() {
		return HttpRequestFactoryHolder.INSTANCE;
	}
	
	public static HttpRequestFactory getRequestFactoryNoSslValidation() {
		return HttpRequestFactoryNoSslValidationHolder.INSTANCE;
	}
}
