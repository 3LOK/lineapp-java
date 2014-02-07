package org.battlehack.lineapp;

import org.battlehack.lineapp.resource.LineappResource;
import org.battlehack.lineapp.resource.PaypalErrorResource;
import org.battlehack.lineapp.resource.PaypalSuccessResource;
import org.battlehack.lineapp.restlet.AccessControlAllowOriginFilter;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;

public class DefaultApplication extends Application {
	@Override
	public Restlet createInboundRoot() {
		final Router router = new Router(getContext());
		
		router.attach("/v1.0", LineappResource.class);
		router.attach("/paypal/success/{payInternalId}", PaypalSuccessResource.class);
		router.attach("/paypal/error/{payInternalId}", PaypalErrorResource.class);
		
		// Force HTTPS
		final Filter forceHttpsFilter = new ForceHttpsFilter();
		forceHttpsFilter.setNext(router);

		// Support cross-domain XMLHttpRequests
		final Filter accessControlAllowOrigin = new AccessControlAllowOriginFilter();
		accessControlAllowOrigin.setNext(forceHttpsFilter);
		
		return accessControlAllowOrigin;
	}
}
