package org.battlehack.lineapp.restlet;

import org.restlet.representation.Representation;
import org.restlet.resource.Options;
import org.restlet.resource.ServerResource;

public class BaseServerResource extends ServerResource {
	/** Default implementation for HTTP OPTIONS method. */
	@Options
	public Representation describe() {
		return null;
	}
}
