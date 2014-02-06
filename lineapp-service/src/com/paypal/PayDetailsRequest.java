package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayDetailsRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ACTION_TYPE_PAY = "PAY";
	
    /** Default constructor for JSON deserialization. */
    public PayDetailsRequest() {}
	
    public PayDetailsRequest(String payKey, RequestEnvelope requestEnvelope) {
    	this.payKey = payKey;
    	this.requestEnvelope = requestEnvelope;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;
    
    @JsonInclude(Include.NON_NULL)
    public RequestEnvelope requestEnvelope;
}