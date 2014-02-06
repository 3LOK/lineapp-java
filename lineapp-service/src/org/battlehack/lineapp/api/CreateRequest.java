package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateRequest extends Request {
    public static final String TYPE = "create";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public CreateRequest() {}
    
    public CreateRequest(String accessToken, CreateEvent info) {
    	this.accessToken = accessToken;
    	this.info = info;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String accessToken;
    
    @JsonInclude(Include.NON_NULL)
    public CreateEvent info;
    
	public void validate() throws LineappException {
		if (info == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid info"));
		}
		info.validate();
	}
}
