package org.battlehack.lineapp.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetPaymentStatusRequest extends Request {
    public static final String TYPE = "get_payment_status";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public GetPaymentStatusRequest() {}
    
    public GetPaymentStatusRequest(String payKey) {
    	this.payKey = payKey;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String payKey;
    
	public void validate() throws LineappException {
		if ((payKey == null) || (payKey.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid payKey"));
		}
	}
}
