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
		final LineState lineState = new LineState();
		for (Event event : line.events) {
			lineState.update(event);
		}
		
		if (!request.events.isEmpty()) {
			// Validate permissions
			final ClientId clientId = FacebookAuthenticator.authenticate(request.accessToken);
			if (clientId == null) {
				throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "authenticated user required"));
			}
			
			for (Event event : request.events) {
				if (event instanceof CreateEvent) {
					// Intentionally left blank
				} else if (event instanceof JoinEvent) {
					final JoinEvent joinEvent = (JoinEvent) event;
					if (joinEvent.friendClientId != null) {
						if (!lineState.info.israeli) {
							throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "can't use friends in non israeli line"));
						}
						// TODO: validate real (Facebook) friends
					}
					
				} else if (event instanceof LeaveEvent) {
					final LeaveEvent leaveEvent = (LeaveEvent) event;
					
					if ((!clientId.equals(leaveEvent.clientId)) && (!lineState.info.managers.contains(clientId))) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can make other user leave"));
					}
					
				} else if (event instanceof SetPriceEvent) {
					final SetPriceEvent setPriceEvent = (SetPriceEvent) event;
					if (!clientId.equals(setPriceEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot set price of other user"));
					}
					
				} else if (event instanceof SwapEvent) {
					final SwapEvent swapEvent = (SwapEvent) event;
					if (!clientId.equals(swapEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot swap other user"));
					}
					// TODO: validate payment made
					
				} else if (event instanceof MoveToVipEvent) {
					final MoveToVipEvent moveToVipEvent = (MoveToVipEvent) event;
					if (!clientId.equals(moveToVipEvent.clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "cannot move other user to vip"));
					}
					
					if (lineState.info.vipPrice == null) {
						throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "line does not support vip"));
					}
					
					// TODO: validate sender?
					Pay.validatePaymentDone(null, Collections.singletonList(lineState.info.vipPrice), moveToVipEvent.payKey);
					
				} else if (event instanceof HandleEvent) {
//					final HandleEvent handleEvent = (HandleEvent) event;
					if (!lineState.info.managers.contains(clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can handle users"));
					}

				} else if (event instanceof HandledEvent) {
//					final HandledEvent handledEvent = (HandledEvent) event;
					if (!lineState.info.managers.contains(clientId)) {
						throw new LineappException(new Error(Error.ERROR_NO_PERMISSION, "only managers can mark users as handled"));
					}
					
				} else {
					throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "unsupported event type: " + event.getClass()));
				}
				lineState.update(event);
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
