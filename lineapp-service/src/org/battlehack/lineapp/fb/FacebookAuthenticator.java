package org.battlehack.lineapp.fb;

import org.battlehack.lineapp.api.ExtendRequest;
import org.battlehack.lineapp.api.ExtendedAccessToken;
import org.battlehack.lineapp.api.LineappException;

public class FacebookAuthenticator {
	private FacebookAuthenticator() {}
	
	private static final String FACEBOOK_APP_ID = "471521269618702";
	private static final String FACEBOOK_APP_SECRET = "XXX"; // real value is secret
	
	private static final AccessTokenExtender extender = new AccessTokenExtender(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET);
	
	public static ExtendedAccessToken extend(ExtendRequest request) throws LineappException {
		request.validate();
		
		return extender.extend(request.accessToken);
	}
}
