package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public PaymentInfo() {}
    
    @JsonInclude(Include.NON_NULL)
    public Receiver receiver;
}
