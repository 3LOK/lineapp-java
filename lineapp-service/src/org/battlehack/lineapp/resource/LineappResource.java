package org.battlehack.lineapp.resource;

import java.io.IOException;
import java.io.InputStream;

import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.Request;
import org.battlehack.lineapp.api.Response;
import org.battlehack.lineapp.json.Json;
import org.battlehack.lineapp.restlet.BaseServerResource;
import org.battlehack.lineapp.restlet.Utils;
import org.battlehack.lineapp.state.LineappService;
import org.restlet.data.MediaType;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;

import com.fasterxml.jackson.core.type.TypeReference;

public class LineappResource extends BaseServerResource {
	/**
	 * HACK: This is a workaround for https://github.com/restlet/restlet-framework-java/issues/699
	 */
	@Post("json")
	public void doPost(Representation value) {
		// TODO: handle null 'value'
		Response<?> response;
		try {
			final org.restlet.Request httpRequest = getRequest();
			
			final Request request;
			final InputStream is = value.getStream();
			try {
				request = Json.parse(is, new TypeReference<Request>() {});
			} finally {
				is.close();
			}
			response = LineappService.execute(request, httpRequest.getClientInfo(), Utils.getXForwarededFor(httpRequest));
		} catch (IOException e) {
			response = Response.fromException(new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid json format"), e));
		}
		
		getResponse().setEntity(Json.stringify(response), MediaType.APPLICATION_JSON);
	}
}
