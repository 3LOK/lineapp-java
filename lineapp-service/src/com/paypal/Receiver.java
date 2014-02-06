package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Receiver implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public Receiver() {}
	
    public Receiver(String email, String amount) {
    	this.email = email;
    	this.amount = amount;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String email;
    
    @JsonInclude(Include.NON_NULL)
    public String amount;
}