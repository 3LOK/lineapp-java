package org.battlehack.lineapp.fb;

import org.battlehack.lineapp.api.ClientId;
import org.battlehack.lineapp.api.ExtendRequest;
import org.battlehack.lineapp.api.ExtendedAccessToken;
import org.battlehack.lineapp.api.LineappException;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookGraphException;
import com.restfb.types.User;

public class FacebookAuthenticator {
	private FacebookAuthenticator() {}
	
	private static final String FACEBOOK_APP_ID = "471521269618702";
	private static final String FACEBOOK_APP_SECRET = "XXX"; // real value is secret
	
	private static final AccessTokenExtender extender = new AccessTokenExtender(FACEBOOK_APP_ID, FACEBOOK_APP_SECRET);
	
	public static ExtendedAccessToken extend(ExtendRequest request) throws LineappException {
		request.validate();
		
		return extender.extend(request.accessToken);
	}
	
	public static ClientId authenticate(String accessToken) {
		if (accessToken == null) {
			return null;
		}
		
		try {
			return new ClientId(ClientId.NS_FACEBOOK, authenticateImpl(accessToken));
		} catch (FacebookGraphException e) {
			// ... graph API error - probably invalid access token
			return null;
		} catch (FacebookException e) {
			// ... communication error
			return null;
		}
	}
	
	private static String authenticateImpl(String accessToken) throws FacebookException {
		final FacebookClient facebookClient = new DefaultFacebookClient(accessToken);
		final User user = facebookClient.fetchObject("me", User.class, Parameter.with("fields", "id"));
		return user.getId();
	}
}
