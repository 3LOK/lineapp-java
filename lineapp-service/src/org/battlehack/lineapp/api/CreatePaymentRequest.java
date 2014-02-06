package org.battlehack.lineapp.api;

import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePaymentRequest extends Request {
    public static final String TYPE = "create_payment";
    private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public CreatePaymentRequest() {}
    
    public CreatePaymentRequest(List<PaymentRequest> paymentRequests) {
    	this.paymentRequests = paymentRequests;
    }
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<PaymentRequest> paymentRequests = new LinkedList<PaymentRequest>();
    
	public void validate() throws LineappException {
		if ((paymentRequests == null) || (paymentRequests.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid paymentRequests"));
		}
		for (PaymentRequest paymentRequest : paymentRequests) {
			paymentRequest.validate();
		}
	}
}
