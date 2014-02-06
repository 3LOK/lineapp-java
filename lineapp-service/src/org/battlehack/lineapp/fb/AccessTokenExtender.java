package org.battlehack.lineapp.fb;

import java.util.HashMap;
import java.util.Map;

import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.ExtendedAccessToken;
import org.battlehack.lineapp.api.LineappException;
import org.slf4j.LoggerFactory;

import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookGraphException;

public class AccessTokenExtender {
	private final String appId;
	private final String appSecret;
	
	public AccessTokenExtender(String appId, String appSecret) {
		this.appId = appId;
		this.appSecret = appSecret;
	}
	
	public ExtendedAccessToken extend(String accessToken) throws LineappException {
		final Map<String, String> response = extendFacebookToken(accessToken);
		final String newAccessToken = response.get("access_token");
		final String expiresStr = response.get("expires");
		Integer expires = null;
		if (expiresStr != null) {
			try {
				expires = Integer.parseInt(expiresStr);
			} catch (NumberFormatException e) {}
		}

		return new ExtendedAccessToken(newAccessToken, expires);
	}
	
	private Map<String, String> extendFacebookToken(String accessToken) throws LineappException {
		try {
			final FacebookClient facebookClient = new DefaultFacebookClient();
			final String response = facebookClient.fetchObject("oauth/access_token", String.class,
					Parameter.with("client_id", appId),
					Parameter.with("client_secret", appSecret),
					Parameter.with("grant_type", "fb_exchange_token"),
					Parameter.with("fb_exchange_token", accessToken));
			return parseQueryString(response);
		} catch (FacebookGraphException e) {
			// graph API error - probably invalid access token or wrong appId
			LoggerFactory.getLogger(AccessTokenExtender.class).warn("Could not extend token: " + accessToken + " (" + appId + ")", e);
			throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "could not extend token"), e);
		} catch (FacebookException e) {
			// communication error
			LoggerFactory.getLogger(AccessTokenExtender.class).warn("Could not extend token: " + accessToken + " (" + appId + ")", e);
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "communication error"), e);
		}
	}
	
	private static Map<String, String> parseQueryString(String query) {
		final Map<String, String> map = new HashMap<String, String>();
		
		final String[] params = query.split("&");
	    for (String param : params) {
	    	final String[] parts = param.split("=");
	    	if (parts.length == 2) {
	    		map.put(parts[0], parts[1]);
	    	}
	    }
	    
	    return map;  		
	}
}
