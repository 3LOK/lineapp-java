package org.battlehack.lineapp.api;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UpdateRequest extends Request {
    public static final String TYPE = "update";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public UpdateRequest() {}
    
    public UpdateRequest(String lineId, Long after, String accessToken, List<Event> events) {
    	this.lineId = lineId;
    	this.after = after;
    	this.accessToken = accessToken;
    	this.events = events;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String lineId;
    
    @JsonInclude(Include.NON_NULL)
    public Long after;
    
    @JsonInclude(Include.NON_NULL)
    public String accessToken;
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<Event> events = new LinkedList<Event>();
    
	public void validate() throws LineappException {
		if ((lineId == null) || (lineId.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid lineId"));
		}
		
		if (events == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid events"));
		}
		for (Event event : events) {
			event.validate();
		}
	}
}
