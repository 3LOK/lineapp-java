package org.battlehack.lineapp.state;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.api.CreatePaymentRequest;
import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.GetPaymentStatusRequest;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.Payment;
import org.battlehack.lineapp.api.PaymentRequest;
import org.battlehack.lineapp.api.PaymentStatus;
import org.battlehack.lineapp.environment.Http;

import com.paypal.Credentials;
import com.paypal.PayDetailsRequest;
import com.paypal.PayDetailsResponse;
import com.paypal.PayRequest;
import com.paypal.PayResponse;
import com.paypal.PaymentInfo;
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
		
		final String payInternalId = UUID.randomUUID().toString();
		
		final PayRequest payRequest = new PayRequest(PayRequest.ACTION_TYPE_PAY, request.paymentRequests.get(0).currency,
				receiverList,
				"https://lineapp-prod.appspot.com/paypal/success/" + payInternalId,
				"https://lineapp-prod.appspot.com/paypal/error/" + payInternalId,
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
		
		pm.makePersistent(new org.battlehack.lineapp.persistent.PaymentStatus(payInternalId, payResponse.payKey));
		
		return new Payment(payResponse.payKey);
	}
	
	public static PaymentStatus getStatus(PersistenceManager pm, GetPaymentStatusRequest request) throws LineappException {
		request.validate();
		
		return new PaymentStatus(request.payKey, org.battlehack.lineapp.persistent.PaymentStatus.queryStatus(pm, request.payKey));
	}
	
	public static void validatePaymentDone(String sender, List<PaymentRequest> paymentRequests, String payKey) throws LineappException {
		// Get payment status
		final PayDetailsResponse payDetailsResponse;
		try {
			payDetailsResponse = paypal.getPaymentDetails(new PayDetailsRequest(payKey,
					new RequestEnvelope("en_US", RequestEnvelope.DETAIL_LEVEL_RETURN_ALL)));
		} catch (IOException e) {
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "paypal communication error"), e);
		}
		
		// Validate state
		if (!PayDetailsResponse.STATUS_COMPLETED.equals(payDetailsResponse.status)) {
			throw new LineappException(new Error(Error.ERROR_BAD_PAYMENT, "payment is not completed: " + payDetailsResponse.status));
		}
		
		// Validate receivers
		final List<PaymentInfo> paymentInfos = payDetailsResponse.paymentInfoList.paymentInfo;
		if (paymentRequests.size() != paymentInfos.size()) {
			throw new LineappException(new Error(Error.ERROR_BAD_PAYMENT, "expected " + paymentRequests.size() + " payemnts, got " + paymentInfos.size()));
		}
		
		final Iterator<PaymentRequest> itExpected = paymentRequests.iterator();
		final Iterator<PaymentInfo> itActual = paymentInfos.iterator();
		while (itExpected.hasNext()) {
			if (!equals(itExpected.next(), itActual.next().receiver)) {
				throw new LineappException(new Error(Error.ERROR_BAD_PAYMENT, "unexpected payment details"));
			}
		}
		
		// TODO: Validate sender (if sender != null)
	}
	
	private static boolean equals(PaymentRequest expected, Receiver actual) {
		if (!expected.destination.equals(actual.email)) {
			return false;
		}
		
		final double expectedAmount = expected.amount.doubleValue() / 100;
		final double actualAmount = Double.parseDouble(actual.amount);
		return (expectedAmount == actualAmount);
	}
}
