package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetEventsRequest extends Request {
    public static final String TYPE = "get_events";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public GetEventsRequest() {}
    
    public GetEventsRequest(String lineId, Long after) {
    	this.lineId = lineId;
    	this.after = after;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String lineId;
    
    @JsonInclude(Include.NON_NULL)
    public Long after;
    
	public void validate() throws LineappException {
		if ((lineId == null) || (lineId.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid lineId"));
		}
	}
}
