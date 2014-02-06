package com.paypal;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestEnvelope implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public static final String DETAIL_LEVEL_RETURN_ALL = "ReturnAll";
	
    /** Default constructor for JSON deserialization. */
    public RequestEnvelope() {}
	
    public RequestEnvelope(String errorLanguage, String detailLevel) {
    	this.errorLanguage = errorLanguage;
    	this.detailLevel = detailLevel;
    }
	
    @JsonInclude(Include.NON_NULL)
    public String errorLanguage;
    
    @JsonInclude(Include.NON_NULL)
    public String detailLevel;
}