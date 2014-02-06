package org.battlehack.lineapp;

import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.json.Json;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.routing.Filter;

public class ForceHttpsFilter extends Filter {
	private static void verifyHttps(Request request) throws LineappException {
		if (!Protocol.HTTPS.equals(request.getProtocol())) {
			throw new LineappException(new Error(Error.ERROR_SECURITY, "You must use https://"));
		}
	}
	
	@Override
	protected int beforeHandle(Request request, Response response) {
		try {
			verifyHttps(request);
			return Filter.CONTINUE;
		} catch (LineappException e) {
			response.setEntity(Json.stringify(org.battlehack.lineapp.api.Response.fromException(e)), MediaType.APPLICATION_JSON);
			return Filter.SKIP;
		}
	}
}
