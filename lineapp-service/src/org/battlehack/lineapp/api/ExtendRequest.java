package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtendRequest extends Request {
    public static final String TYPE = "extend";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public ExtendRequest() {}
    
    public ExtendRequest(String accessToken) {
    	this.accessToken = accessToken;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String accessToken;
    
	public void validate() throws LineappException {
		if ((accessToken == null) || (accessToken.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid accessToken"));
		}
	}
}
