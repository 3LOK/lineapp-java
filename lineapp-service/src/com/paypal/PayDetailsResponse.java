package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayDetailsResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String STATUS_CREATED = "CREATED";
	
    /** Default constructor for JSON deserialization. */
    public PayDetailsResponse() {}
	
    @JsonInclude(Include.NON_NULL)
    public ResponseEnvelope responseEnvelope;
    
    @JsonInclude(Include.NON_NULL)
    public String returnUrl;
    
    @JsonInclude(Include.NON_NULL)
    public String cancelUrl;
    
    @JsonInclude(Include.NON_NULL)
    public String currencyCode;
    
    @JsonInclude(Include.NON_NULL)
    public String status;
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;

    @JsonInclude(Include.NON_NULL)
    public String actionType;

    @JsonInclude(Include.NON_NULL)
    public String feesPayer;
    
    @JsonInclude(Include.NON_NULL)
    public Boolean reverseAllParallelPaymentsOnError;
    
    @JsonInclude(Include.NON_NULL)
    public PaymentInfoList paymentInfoList;
    
//    @JsonInclude(Include.NON_NULL)
//    public Sender sender;
}