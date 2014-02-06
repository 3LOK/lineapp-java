package org.battlehack.lineapp.api;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendEventsRequest extends Request {
    public static final String TYPE = "send_events";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public SendEventsRequest() {}
    
    public SendEventsRequest(String accessToken, List<Event> events) {
    	this.accessToken = accessToken;
    	this.events = events;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String accessToken;
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<Event> events = new LinkedList<Event>();
    
	public void validate() throws LineappException {
		if ((events == null) || (events.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid events"));
		}
	}
}
