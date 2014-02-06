package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Payment implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public Payment() {}
    
    public Payment(String payKey) {
    	this.payKey = payKey;
    }
    
    @Override
	public Object clone() {
    	return new Payment(payKey);
	}
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;
}