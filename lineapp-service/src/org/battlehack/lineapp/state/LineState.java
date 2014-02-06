package org.battlehack.lineapp.state;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.battlehack.lineapp.api.ClientId;
import org.battlehack.lineapp.api.CreateEvent;
import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.Event;
import org.battlehack.lineapp.api.HandleEvent;
import org.battlehack.lineapp.api.HandledEvent;
import org.battlehack.lineapp.api.JoinEvent;
import org.battlehack.lineapp.api.LeaveEvent;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.MoveToVipEvent;
import org.battlehack.lineapp.api.SetPriceEvent;
import org.battlehack.lineapp.api.SwapEvent;

public class LineState {
	public CreateEvent info;
	public final List<Person> people = new LinkedList<Person>();
	public Person handling;
	public final Map<ClientId, Integer> positions = new HashMap<ClientId, Integer>();
	
	public void update(Event event) throws LineappException {
		if (event instanceof CreateEvent) {
			final CreateEvent createEvent = (CreateEvent) event;
			if (info != null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "cannot add create event"));
			}
			info = createEvent;
			
		} else if (event instanceof JoinEvent) {
			final JoinEvent joinEvent = (JoinEvent) event;
			
			if (positions.containsKey(joinEvent.clientId)) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "already in line"));
			}
			
			if (joinEvent.friendClientId != null) {
				final Integer friendPosition = positions.get(joinEvent.friendClientId);
				if (friendPosition == null) {
					throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "friend not in line"));
				}
				int position = friendPosition + 1;
				people.add(position++, new Person(joinEvent.clientId));
				
				final ListIterator<Person> it = people.listIterator(position);
				while (it.hasNext()) {
					final Person nextPerson = it.next();
					positions.put(nextPerson.clientId, position++);
				}
				
			} else {
				people.add(new Person(joinEvent.clientId));
				positions.put(joinEvent.clientId, people.size() - 1);
			}
			
		} else if (event instanceof LeaveEvent) {
			final LeaveEvent leaveEvent = (LeaveEvent) event;
			
			Integer position = positions.get(leaveEvent.clientId);
			if (position == null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
			}
			
			final ListIterator<Person> it = people.listIterator(position);
			it.next();
			it.remove();
			positions.remove(leaveEvent.clientId);
			
			while (it.hasNext()) {
				final Person nextPerson = it.next();
				positions.put(nextPerson.clientId, position++);
			}
			
		} else if (event instanceof SetPriceEvent) {
			final SetPriceEvent setPriceEvent = (SetPriceEvent) event;
			
			final Integer position = positions.get(setPriceEvent.clientId);
			if (position == null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
			}
			people.get(position).price = setPriceEvent.price;
			
		} else if (event instanceof SwapEvent) {
			final SwapEvent swapEvent = (SwapEvent) event;
			
			for (ClientId swapped : swapEvent.clientIds) {
				swap(swapEvent.clientId, swapped);
			}
			
		} else if (event instanceof MoveToVipEvent) {
			final MoveToVipEvent moveToVipEvent = (MoveToVipEvent) event;
			final Integer position = positions.get(moveToVipEvent.clientId);
			if (position == null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
			}
			// TODO: validate not already in vip, move to vip
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "unimplemented event: " + MoveToVipEvent.TYPE));
			
		} else if (event instanceof HandleEvent) {
			final HandleEvent handleEvent = (HandleEvent) event;
			
			final Integer position = positions.get(handleEvent.clientId);
			if (position == null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
			}
			if (!position.equals(0)) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "can only handle first user"));
			}
			
			if (handling != null) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "can only handle one person at a time"));
			}
			
			handling = people.remove(0);
			positions.remove(handleEvent.clientId);
			
			for (Entry<ClientId, Integer> entry : positions.entrySet()) {
				entry.setValue(entry.getValue() - 1);
			}
			
			// TODO: validate vip line is empty

		} else if (event instanceof HandledEvent) {
			final HandledEvent handledEvent = (HandledEvent) event;
			
			if (!handledEvent.clientId.equals(handling)) {
				throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "user is not currently being handled"));
			}
			
			handling = null;
			
		} else {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "unsupported event type: " + event.getClass()));
		}
	}
	
	private void swap(ClientId clientId1, ClientId clientId2) throws LineappException {
		final Integer position1 = positions.get(clientId1);
		if (position1 == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
		}
		
		final Integer position2  = positions.get(clientId2);
		if (position2 == null) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "not in line"));
		}
		
		if (position1.intValue() - position2.intValue() != 1) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "can only swap with person in front of you"));
		}
		
		final Person person1 = people.get(position1);
		final Person person2 = people.get(position2);
		
		people.set(position2, person1);
		people.set(position1, person2);
		
		positions.put(clientId1, position2);
		positions.put(clientId2, position1);
	}
}
