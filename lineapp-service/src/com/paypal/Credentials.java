package com.paypal;

public class Credentials {
	public final String appId;
	public final String userId;
	public final String password;
	public final String signature;
	
	public Credentials(String appId, String userId, String password, String signature) {
		this.appId = appId;
		this.userId = userId;
		this.password = password;
		this.signature = signature;
	}

}
