package org.battlehack.lineapp.api;

import java.net.MalformedURLException;
import java.net.URL;
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
    
    public CreatePaymentRequest(List<PaymentRequest> paymentRequests, String successUrl, String errorUrl) {
    	this.paymentRequests = paymentRequests;
    	this.successUrl = successUrl;
    	this.errorUrl = errorUrl;
    }
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<PaymentRequest> paymentRequests = new LinkedList<PaymentRequest>();
    
    @JsonInclude(Include.NON_NULL)
    public String successUrl;
    
    @JsonInclude(Include.NON_NULL)
    public String errorUrl;
    
	public void validate() throws LineappException {
		if ((paymentRequests == null) || (paymentRequests.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid paymentRequests"));
		}
		for (PaymentRequest paymentRequest : paymentRequests) {
			paymentRequest.validate();
		}
		
		if (successUrl == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid successUrl"));
		}
		try {
			new URL(successUrl);
		} catch (MalformedURLException e) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid successUrl: " + successUrl), e);
		}
		
		if (errorUrl == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid errorUrl"));
		}
		try {
			new URL(errorUrl);
		} catch (MalformedURLException e) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid errorUrl: " + errorUrl), e);
		}
	}
}
