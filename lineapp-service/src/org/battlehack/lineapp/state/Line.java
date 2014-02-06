package org.battlehack.lineapp.state;

import java.util.Collections;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.api.ClientId;
import org.battlehack.lineapp.api.CreateEvent;
import org.battlehack.lineapp.api.CreateRequest;
import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.Event;
import org.battlehack.lineapp.api.Events;
import org.battlehack.lineapp.api.HandleEvent;
import org.battlehack.lineapp.api.HandledEvent;
import org.battlehack.lineapp.api.JoinEvent;
import org.battlehack.lineapp.api.LeaveEvent;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.MoveToVipEvent;
import org.battlehack.lineapp.api.SetPriceEvent;
import org.battlehack.lineapp.api.SwapEvent;
import org.battlehack.lineapp.api.UpdateRequest;
import org.battlehack.lineapp.fb.FacebookAuthenticator;

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
	
	public static Events update(PersistenceManager pm, UpdateRequest request) throws LineappException {
		request.validate();
		
		final org.battlehack.lineapp.persistent.Line persistLine =
				org.battlehack.lineapp.persistent.Line.byIdNoThrow(pm, request.lineId);
		if (persistLine == null) {
			throw new LineappException(new Error(Error.ERROR_NOT_FOUND, "unknown lineId: " + request.lineId));
		}
		
		final org.battlehack.lineapp.api.Line line = persistLine.get();
		final CreateEvent lineInfo = (CreateEvent) line.events.get(0); // create event is always first
		
		if (!request.events.isEmpty()) {
			// Validate permissions
			final ClientId clientId = FacebookAuthenticator.authenticate(request.accessToken);
			if (clientId == null) {
				throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "authenticated user required"));
			}
			
			for (Event event : request.events) {
				if (event instanceof CreateEvent) {
					throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "cannot add create event"));
				} else if (event instanceof JoinEvent) {
					final JoinEvent joinEvent = (JoinEvent) event;
					// TODO: validate join event (not already in line, validate friend, validate friend in line)
					if ((joinEvent.friendClientId != null) && (!lineInfo.israeli)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "can't use friends in non israeli line"));
					}
					
				} else if (event instanceof LeaveEvent) {
					final LeaveEvent leaveEvent = (LeaveEvent) event;
					// TODO: validate (in line)
					
					if ((!clientId.equals(leaveEvent.clientId)) && (!lineInfo.managers.contains(clientId))) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can make other user leave"));
					}
					// TODO: validate (in line)
					
				} else if (event instanceof SetPriceEvent) {
					final SetPriceEvent setPriceEvent = (SetPriceEvent) event;
					if (!clientId.equals(setPriceEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot set price of other user"));
					}
					// TODO: validate (in line)
					
				} else if (event instanceof SwapEvent) {
					final SwapEvent swapEvent = (SwapEvent) event;
					if (!clientId.equals(swapEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot swap other user"));
					}
					// TODO: validate (all in line, directly after each other, payment made)
					
				} else if (event instanceof MoveToVipEvent) {
					final MoveToVipEvent moveToVipEvent = (MoveToVipEvent) event;
					if (!clientId.equals(moveToVipEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot move other user to vip"));
					}
					// TODO: validate (in line, not already in vip, payment made)
					
				} else if (event instanceof HandleEvent) {
					final HandleEvent handleEvent = (HandleEvent) event;
					if (!lineInfo.managers.contains(clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can handle users"));
					}
					// TODO: validate (in line, first in line, vip line empty)

				} else if (event instanceof HandledEvent) {
					final HandledEvent handledEvent = (HandledEvent) event;
					if (!lineInfo.managers.contains(clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can mark users as handled"));
					}
					// TODO: validate (being handled)
					
				} else {
					throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "unsupported event type: " + event.getClass()));
				}
			}
			
			line.events.addAll(request.events);
			persistLine.set(line);
		}
		
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
}
