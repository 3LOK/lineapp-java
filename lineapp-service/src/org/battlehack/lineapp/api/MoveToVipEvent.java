package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MoveToVipEvent extends Event {
    public static final String TYPE = "move_to_vip";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public MoveToVipEvent() {}
    
    public MoveToVipEvent(Long timestamp, ClientId clientId, String payKey) {
    	super(timestamp);
    	
    	this.clientId = clientId;
    	this.payKey = payKey;
    }
    
    @Override
	public Object clone() {
    	return new MoveToVipEvent(timestamp,
    			((clientId != null) ? (ClientId) clientId.clone() : null),
    			payKey);
    }
    
    @JsonInclude(Include.NON_NULL)
    public ClientId clientId;
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;
    
	public void validate() throws LineappException {
		super.validate();
		
		if (clientId == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientId"));
		}
		clientId.validate();
		
		if ((payKey == null) || (payKey.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid payKey"));
		}
	}
}
