package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseEnvelope implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String ACK_SUCCESS = "Success";
	public static final String ACK_FAILURE = "Failure";
	
    /** Default constructor for JSON deserialization. */
    public ResponseEnvelope() {}
	
    @JsonInclude(Include.NON_NULL)
    public String timestamp;
    
    @JsonInclude(Include.NON_NULL)
    public String ack;

    @JsonInclude(Include.NON_NULL)
    public String correlationId;

    @JsonInclude(Include.NON_NULL)
    public String build;
}