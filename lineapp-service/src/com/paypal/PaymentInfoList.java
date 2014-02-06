package com.paypal;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentInfoList implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public PaymentInfoList() {}
    
    @JsonInclude(Include.NON_NULL)
    public List<PaymentInfo> paymentInfo;
}