package com.paypal;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PayResponse implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public PayResponse() {}
	
    @JsonInclude(Include.NON_NULL)
    public ResponseEnvelope responseEnvelope;
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;

    @JsonInclude(Include.NON_NULL)
    public String paymentExecStatus;
    
    @JsonInclude(Include.NON_NULL)
    public List<Error> error;
}