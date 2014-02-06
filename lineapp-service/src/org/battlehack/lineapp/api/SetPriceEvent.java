package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SetPriceEvent extends Event {
    public static final String TYPE = "set_price";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public SetPriceEvent() {}
    
    public SetPriceEvent(Long timestamp, ClientId clientId, PaymentRequest price) {
    	super(timestamp);
    	
    	this.clientId = clientId;
    	this.price = price;
    }
    
    @Override
	public Object clone() {
    	return new SetPriceEvent(timestamp,
    			((clientId != null) ? (ClientId) clientId.clone() : null),
    			((price != null) ? (PaymentRequest) price.clone() : null));
    }
    
    @JsonInclude(Include.NON_NULL)
    public ClientId clientId;
    
    @JsonInclude(Include.NON_NULL)
    public PaymentRequest price;
    
	public void validate() throws LineappException {
		super.validate();
		
		if (clientId == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientId"));
		}
		clientId.validate();
		
		if (price != null) {
			price.validate();
		}
	}
}
