package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JoinEvent extends Event {
    public static final String TYPE = "join";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public JoinEvent() {}
    
    public JoinEvent(Long timestamp, ClientId clientId, ClientId friendClientId) {
    	super(timestamp);
    	
    	this.clientId = clientId;
    	this.friendClientId = friendClientId;
    }
    
    @Override
	public Object clone() {
    	return new JoinEvent(timestamp,
    			((clientId != null) ? (ClientId) clientId.clone() : null),
    			((friendClientId != null) ? (ClientId) friendClientId.clone() : null));
    }
    
    @JsonInclude(Include.NON_NULL)
    public ClientId clientId;
    
    @JsonInclude(Include.NON_DEFAULT)
    public ClientId friendClientId;
    
    
	public void validate() throws LineappException {
		super.validate();
		
		if (clientId == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientId"));
		}
		clientId.validate();
		
		if (friendClientId != null) {
			friendClientId.validate();
		}
	}
}
