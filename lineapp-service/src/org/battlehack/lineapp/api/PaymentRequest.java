package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class PaymentRequest implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
    
    public PaymentRequest(String destination, Integer amount, String currency) {
    	this.destination = destination;
        this.amount = amount;
        this.currency = currency;
    }
    
    /** Default constructor for JSON deserialization. */
    public PaymentRequest() {}
    
    @Override
	public Object clone() {
    	return new PaymentRequest(destination, amount, currency);
	}
    
    /** The unique id's namespace. */
    @JsonInclude(Include.NON_NULL)
    public String destination;
	
    /** Amount to pay (in "cents"). */
    @JsonInclude(Include.NON_NULL)
    public Integer amount;
    
    /** Currency (ISO 4217). */
    @JsonInclude(Include.NON_NULL)
    public String currency;
    
	public void validate() throws LineappException {
		if ((destination == null) || (destination.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid destination"));
		}

		if (amount == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid amount"));
		}
		if (amount <= 0) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "amount must be positive: " + amount));
		}
		
		if ((currency == null) || (currency.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid currency"));
		}
	}
}