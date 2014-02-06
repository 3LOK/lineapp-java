package org.battlehack.lineapp.restlet;

import org.restlet.Request;
import org.restlet.engine.header.Header;
import org.restlet.util.Series;

public class Utils {
	private Utils() {}
	
	/**
	 * Extracts the "X-Forwarded-For" HTTP header from a Restlet request.
	 * 
	 * This is a workaround for request.getClientInfo().getForwardedAddresses()
	 * requiring to set a special "useForwardedForHeader" parameter to "true",
	 * as it doesn't seem to work under AppEngine.
	 * 
	 * @param request a Restlet request object
	 * @return the "X-Forwarded-For" HTTP header for this request (can be null).
	 */
	public static String getXForwarededFor(Request request) {
		return getHttpHeader(request, "X-Forwarded-For");
	}
	
	/**
	 * Extracts the "X-Facebook-Locale" HTTP header from a Restlet request.
	 * @see https://developers.facebook.com/docs/beta/opengraph/internationalization/
	 * 
	 * @param request a Restlet request object
	 * @return the "X-Facebook-Locale" HTTP header for this request (can be null).
	 */
	public static String getXFacebookLocale(Request request) {
		return getHttpHeader(request, "X-Facebook-Locale");
	}
	
	private static String getHttpHeader(Request request, String name) {
		@SuppressWarnings("unchecked")
		final Series<Header> requestHeaders = (Series<Header>) request.getAttributes().get("org.restlet.http.headers");
		return ((requestHeaders != null) ? requestHeaders.getFirstValue(name) : null);
	}
}
