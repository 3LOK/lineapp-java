package org.battlehack.lineapp.api;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateEvent extends Event {
    public static final String TYPE = "create";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public CreateEvent() {}
    
    public CreateEvent(Long timestamp, List<ClientId> managers, PaymentRequest vipPrice, Boolean israeli) {
    	super(timestamp);
    	
    	this.managers = managers;
    	this.vipPrice = vipPrice;
    	this.israeli = israeli;
    }
    
    @Override
	public Object clone() {
    	final List<ClientId> clonedManagers;
    	if (managers != null) {
    		clonedManagers = new LinkedList<ClientId>();
    		for (ClientId manager : managers) {
    			clonedManagers.add((ClientId) manager.clone());
    		}
    	} else {
    		clonedManagers = null;
    	}
    	return new CreateEvent(timestamp, clonedManagers,
    			((vipPrice != null) ? (PaymentRequest) vipPrice.clone() : null),
    			israeli);
    }
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<ClientId> managers = new LinkedList<ClientId>();
    
    @JsonInclude(Include.NON_NULL)
    public PaymentRequest vipPrice;
    
    @JsonInclude(Include.NON_DEFAULT)
    public Boolean israeli = Boolean.FALSE;
    
    
	public void validate() throws LineappException {
		super.validate();
		
		if ((managers == null) || (managers.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid managers"));
		}
		for (ClientId manager : managers) {
			manager.validate();
		}
		
		if (vipPrice != null) {
			vipPrice.validate();
		}
		
		if (israeli == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid israeli"));
		}
	}
}
