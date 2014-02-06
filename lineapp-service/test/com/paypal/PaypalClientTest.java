package com.paypal;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Test;

import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.paypal.PaypalClient.Endpoint;

public class PaypalClientTest {
	private static final HttpRequestFactory requestFactory = new NetHttpTransport().createRequestFactory();
	private static final Credentials credentials = new Credentials("APP-80W284485P519543T",
			"XXX", "PPP", "SSS");

	@Test
	public void testEmptyReceivers() throws Exception {
		final PaypalClient paypal = new PaypalClient(requestFactory, 10000, 10000, Endpoint.SANDBOX, credentials);
		final ReceiverList receiverList = new ReceiverList();
		final PayRequest payRequest = new PayRequest(PayRequest.ACTION_TYPE_PAY, "USD", receiverList,
				"https://lineapp-prod.appspot.com/paypal_success",
				"https://lineapp-prod.appspot.com/paypal_failure",
				new RequestEnvelope("en_US", RequestEnvelope.DETAIL_LEVEL_RETURN_ALL));
		
		final PayResponse payResponse = paypal.pay(payRequest);
		assertNotNull(payResponse.responseEnvelope);
		assertEquals(ResponseEnvelope.ACK_FAILURE, payResponse.responseEnvelope.ack);
		assertNull(payResponse.payKey);
		assertNotNull(payResponse.error);
		assertFalse(payResponse.error.isEmpty());
	}
	
	@Test
	public void testOneReceiver() throws Exception {
		final PaypalClient paypal = new PaypalClient(requestFactory, 10000, 10000, Endpoint.SANDBOX, credentials);
		final ReceiverList receiverList = new ReceiverList(Collections.singletonList(new Receiver("info@example.com", "10.0")));
		final PayRequest payRequest = new PayRequest(PayRequest.ACTION_TYPE_PAY, "USD", receiverList,
				"https://lineapp-prod.appspot.com/paypal_success",
				"https://lineapp-prod.appspot.com/paypal_failure",
				new RequestEnvelope("en_US", RequestEnvelope.DETAIL_LEVEL_RETURN_ALL));
		
		final PayResponse payResponse = paypal.pay(payRequest);
		assertNotNull(payResponse.responseEnvelope);
		assertEquals(ResponseEnvelope.ACK_SUCCESS, payResponse.responseEnvelope.ack);
		assertNotNull(payResponse.payKey);
	}
}
