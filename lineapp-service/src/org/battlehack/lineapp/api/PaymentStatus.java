package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PaymentStatus implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
    
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";
    
    public PaymentStatus(String payKey, String status) {
    	this.payKey = payKey;
    	this.status = status;
    }
    
    /** Default constructor for JSON deserialization. */
    public PaymentStatus() {}
    
    @Override
	public Object clone() {
    	return new PaymentStatus(payKey, status);
	}
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;
	
    @JsonInclude(Include.NON_NULL)
    public String status;
}