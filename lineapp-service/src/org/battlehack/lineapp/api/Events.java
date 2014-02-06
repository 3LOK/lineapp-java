package org.battlehack.lineapp.api;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Events implements Serializable {
	private static final long serialVersionUID = 1L;
    
    public static final String NS_LINEAPP = "org.battlehack.lineapp";
    public static final String NS_FACEBOOK = "com.facebook";
    
    public Events(List<Event> events) {
    	this.events = events;
    }
    
    /** Default constructor for JSON deserialization. */
    public Events() {}
    
    /** The unique id's namespace. */
    @JsonInclude(Include.NON_DEFAULT)
    public List<Event> events = new LinkedList<Event>();
}