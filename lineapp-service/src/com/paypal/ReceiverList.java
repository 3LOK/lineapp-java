package com.paypal;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiverList implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public ReceiverList() {}
    
    /** Default constructor for JSON deserialization. */
    public ReceiverList(List<Receiver> receiver) {
    	this.receiver = receiver;
    }
	
    @JsonInclude(Include.NON_NULL)
    public List<Receiver> receiver;
}