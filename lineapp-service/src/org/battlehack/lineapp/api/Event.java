package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,  
	    include = JsonTypeInfo.As.PROPERTY,  
	    property = "type")  
@JsonSubTypes({
	@Type(value = CreateEvent.class, name = CreateEvent.TYPE),
	@Type(value = JoinEvent.class, name = JoinEvent.TYPE),
	@Type(value = LeaveEvent.class, name = LeaveEvent.TYPE),
	@Type(value = SetPriceEvent.class, name = SetPriceEvent.TYPE),
	@Type(value = SwapEvent.class, name = SwapEvent.TYPE),
	@Type(value = MoveToVipEvent.class, name = MoveToVipEvent.TYPE),
	@Type(value = HandleEvent.class, name = HandleEvent.TYPE),
	@Type(value = HandledEvent.class, name = HandledEvent.TYPE)
})
public abstract class Event implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public Event() {}
    
    /** Default constructor for JSON deserialization. */
    public Event(Long timestamp) {
    	this.timestamp = timestamp;
    }
    
    @Override
	public abstract Object clone();
    
    @JsonInclude(Include.NON_NULL)
    public Long timestamp;
    
    public void validate() throws LineappException {
    	timestamp = System.currentTimeMillis();
    }
}
