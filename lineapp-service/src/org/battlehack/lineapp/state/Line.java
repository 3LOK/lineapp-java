package org.battlehack.lineapp.state;

import java.util.Collections;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.api.CreateRequest;
import org.battlehack.lineapp.api.Event;
import org.battlehack.lineapp.api.Events;
import org.battlehack.lineapp.api.GetEventsRequest;
import org.battlehack.lineapp.api.LineappException;

public class Line {
	private Line() {}
	
	public static org.battlehack.lineapp.api.Line create(PersistenceManager pm, CreateRequest request) throws LineappException {
		request.validate();
		
		final org.battlehack.lineapp.api.Line line = new org.battlehack.lineapp.api.Line(null,
				Collections.<Event>singletonList(request.info));
		
		final org.battlehack.lineapp.persistent.Line persistLine = 
				new org.battlehack.lineapp.persistent.Line(line);
		
		pm.makePersistent(persistLine);
		return persistLine.get();
	}
	
	public static Events getEvents(PersistenceManager pm, GetEventsRequest request) throws LineappException {
		request.validate();
		
		final org.battlehack.lineapp.api.Line line = org.battlehack.lineapp.persistent.Line.get(pm, request.lineId);
		
		final Events events = new Events();
		if (request.after == null) {
			events.events.addAll(line.events);
		} else {
			for (Event event : line.events) {
				if (event.timestamp > request.after) {
					events.events.add(event);
				}
			}
		}
		return events;
	}

//	public Events sendEvents(PersistenceManager pm, SendEventsRequest request) throws LineappException {
//		request.validate();
//		
//		// TODO: get clientId
//	}
}
