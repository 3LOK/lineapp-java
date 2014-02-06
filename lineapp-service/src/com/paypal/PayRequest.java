package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ACTION_TYPE_PAY = "PAY";
	
    /** Default constructor for JSON deserialization. */
    public PayRequest() {}
	
    public PayRequest(String actionType, String currencyCode, ReceiverList receiverList, String returnUrl, String cancelUrl,
    		RequestEnvelope requestEnvelope) {
    	this.actionType = actionType;
    	this.currencyCode = currencyCode;
    	this.receiverList = receiverList;
    	this.returnUrl = returnUrl;
    	this.cancelUrl = cancelUrl;
    	this.requestEnvelope = requestEnvelope;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String actionType;
    
    @JsonInclude(Include.NON_NULL)
    public String currencyCode;

    @JsonInclude(Include.NON_NULL)
    public ReceiverList receiverList;
    
    @JsonInclude(Include.NON_NULL)
    public String returnUrl;
    
    @JsonInclude(Include.NON_NULL)
    public String cancelUrl;
    
    @JsonInclude(Include.NON_NULL)
    public RequestEnvelope requestEnvelope;
}