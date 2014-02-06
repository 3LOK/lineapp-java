package org.battlehack.lineapp.state;

import org.battlehack.lineapp.api.ClientId;
import org.battlehack.lineapp.api.PaymentRequest;

public class Person {
	public final ClientId clientId;
	public PaymentRequest price;
	
	public Person(ClientId clientId) {
		this.clientId = clientId;
	}
}
