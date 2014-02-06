package org.battlehack.lineapp.state;

import java.io.IOException;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.api.CreatePaymentRequest;
import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.Payment;
import org.battlehack.lineapp.api.PaymentRequest;
import org.battlehack.lineapp.environment.Http;

import com.paypal.Credentials;
import com.paypal.PayRequest;
import com.paypal.PayResponse;
import com.paypal.PaypalClient;
import com.paypal.PaypalClient.Endpoint;
import com.paypal.Receiver;
import com.paypal.ReceiverList;
import com.paypal.RequestEnvelope;
import com.paypal.ResponseEnvelope;

public class Pay {
	private Pay() {}
	
	private static final Credentials credentials = new Credentials("APP-80W284485P519543T",
			"XXX", "PPP", "SSS");
	
	private static final PaypalClient paypal = new PaypalClient(
			Http.getRequestFactory(), Http.CONNECT_TIMEOUT, Http.READ_TIMEOUT, Endpoint.SANDBOX, credentials);
	
	public static Payment create(PersistenceManager pm, CreatePaymentRequest request) throws LineappException {
		request.validate();
		
		// TODO: currency is assumed to be the same for all
		final ReceiverList receiverList = new ReceiverList();
		receiverList.receiver = new LinkedList<Receiver>();
		for (PaymentRequest paymentRequest : request.paymentRequests) {
			receiverList.receiver.add(new Receiver(paymentRequest.destination,
					Double.toString(paymentRequest.amount.doubleValue() / 100)));
		}
		final PayRequest payRequest = new PayRequest(PayRequest.ACTION_TYPE_PAY, request.paymentRequests.get(0).currency,
				receiverList,
				"https://lineapp-prod.appspot.com/paypal_success",
				"https://lineapp-prod.appspot.com/paypal_failure",
				new RequestEnvelope("en_US", RequestEnvelope.DETAIL_LEVEL_RETURN_ALL));
		
		final PayResponse payResponse;
		try {
			payResponse = paypal.createPayment(payRequest);
		} catch (IOException e) {
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "paypal communication error"), e);
		}
		if (!ResponseEnvelope.ACK_SUCCESS.equals(payResponse.responseEnvelope.ack)) {
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "paypal ack: " + payResponse.responseEnvelope.ack));
		}
		
		return new Payment(payResponse.payKey);
	}
}
