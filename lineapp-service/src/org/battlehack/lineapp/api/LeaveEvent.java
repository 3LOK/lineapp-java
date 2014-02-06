package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LeaveEvent extends Event {
    public static final String TYPE = "leave";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public LeaveEvent() {}
    
    public LeaveEvent(Long timestamp, ClientId clientId) {
    	super(timestamp);
    	
    	this.clientId = clientId;
    }
    
    @Override
	public Object clone() {
    	return new LeaveEvent(timestamp,
    			((clientId != null) ? (ClientId) clientId.clone() : null));
    }
    
    @JsonInclude(Include.NON_NULL)
    public ClientId clientId;
    
	public void validate() throws LineappException {
		super.validate();
		
		if (clientId == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientId"));
		}
		clientId.validate();
	}
}
