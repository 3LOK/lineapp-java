package com.paypal;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Error implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** Default constructor for JSON deserialization. */
    public Error() {}
	
    @JsonInclude(Include.NON_NULL)
    public String errorId;
    
    @JsonInclude(Include.NON_NULL)
    public String domain;

    @JsonInclude(Include.NON_NULL)
    public String subdomain;
    
    @JsonInclude(Include.NON_NULL)
    public String severity;

    @JsonInclude(Include.NON_NULL)
    public String category;

    @JsonInclude(Include.NON_NULL)
    public String message;
    
    @JsonInclude(Include.NON_NULL)
    public List<String> parameter;
}