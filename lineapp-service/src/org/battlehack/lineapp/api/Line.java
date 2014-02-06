package org.battlehack.lineapp.api;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class Line implements Serializable {
	private static final long serialVersionUID = 1L;
    
    /** Default constructor for JSON deserialization. */
    public Line() {}
    
    public Line(String id, List<Event> events) {
    	this.id = id;
    	this.events = events;
    }
    
    @JsonInclude(Include.NON_NULL)
    public String id;
    
    @JsonInclude(Include.NON_DEFAULT)
    public List<Event> events = new LinkedList<Event>();
}