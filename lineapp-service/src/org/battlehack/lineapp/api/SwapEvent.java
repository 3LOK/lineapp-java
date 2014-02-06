package org.battlehack.lineapp.api;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SwapEvent extends Event {
    public static final String TYPE = "swap";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public SwapEvent() {}
    
    public SwapEvent(Long timestamp, ClientId clientId, List<ClientId> clientIds) {
    	super(timestamp);
    	
    	this.clientId = clientId;
    	this.clientIds = clientIds;
    }
    
    @Override
	public Object clone() {
    	final List<ClientId> clonedClientIds;
    	if (clientIds != null) {
    		clonedClientIds = new LinkedList<ClientId>();
    		for (ClientId theClientId : clientIds) {
    			clonedClientIds.add((ClientId) theClientId.clone());
    		}
    	} else {
    		clonedClientIds = null;
    	}
    	return new SwapEvent(timestamp,
    			((clientId != null) ? (ClientId) clientId.clone() : null),
    			clonedClientIds);
    }
    
    @JsonInclude(Include.NON_NULL)
    public ClientId clientId;
    
    @JsonInclude(Include.NON_NULL)
    public List<ClientId> clientIds;
    
	public void validate() throws LineappException {
		super.validate();
		
		if (clientId == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientId"));
		}
		clientId.validate();
		
		if ((clientIds == null) || (clientIds.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid clientIds"));
		}
		for (ClientId theClientId : clientIds) {
			theClientId.validate();
		}
	}
}
