package org.battlehack.lineapp.restlet;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.restlet.Request;
import org.restlet.Response;
import org.restlet.engine.header.Header;
import org.restlet.routing.Filter;
import org.restlet.util.Series;

public class AccessControlAllowOriginFilter extends Filter {
	/** @see http://saltybeagle.com/2009/09/cross-origin-resource-sharing-demo/ */
	private static final List<Header> HEADERS = Arrays.asList(
			new Header("Access-Control-Allow-Origin", "*"),
			new Header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS"),
			new Header("Access-Control-Allow-Headers",
					"Cache-Control, Content-Language, Content-Type, Last-Modified, Expires, Pragma, X-Requested-With"),
			new Header("Access-Control-Max-Age", "31536000") // allow caching for 365 days
			);
	
	@Override
	protected void afterHandle(Request request, Response response) {
		final Map<String, Object> responseAttributes = response.getAttributes();
		@SuppressWarnings("unchecked")
		Series<Header> responseHeaders = (Series<Header>) responseAttributes.get("org.restlet.http.headers");
		if (responseHeaders == null) {
			responseHeaders = new Series<Header>(Header.class);
			responseAttributes.put("org.restlet.http.headers", responseHeaders);
		}
		responseHeaders.addAll(HEADERS);
	}
}
