package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response<T> implements Serializable {
	private static final long serialVersionUID = 1L;
	
    /** The error value, if one occurred. */
    @JsonInclude(Include.NON_NULL)
    public Error error;

    /** The return value, if no error occurred. */
    @JsonInclude(Include.NON_NULL)
    public T value;

    public Response(T value) {
        this.value = value;
    }
    
    public Response(Error error) {
        this.error = error;
    }

    /** Default constructor for JSON deserialization. */
    public Response() {}

    @SuppressWarnings("rawtypes")
	public static Response fromNullValue() {
        return new Response();
    }
    
	@SuppressWarnings("rawtypes")
	public static Response fromException(LineappException e) {
        return new Response(e.getError());
    }
}
