package org.battlehack.lineapp.state;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.api.CreatePaymentRequest;
import org.battlehack.lineapp.api.CreateRequest;
import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.Events;
import org.battlehack.lineapp.api.ExtendRequest;
import org.battlehack.lineapp.api.ExtendedAccessToken;
import org.battlehack.lineapp.api.GetPaymentStatusRequest;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.api.Payment;
import org.battlehack.lineapp.api.PaymentStatus;
import org.battlehack.lineapp.api.Request;
import org.battlehack.lineapp.api.Response;
import org.battlehack.lineapp.api.UpdateRequest;
import org.battlehack.lineapp.json.Json;
import org.battlehack.lineapp.persistent.PMF;
import org.restlet.data.ClientInfo;
import org.slf4j.LoggerFactory;

import com.google.apphosting.api.DeadlineExceededException;

public class LineappService {
	private LineappService() {}
	
	public static Response<?> execute(PersistenceManager pm, Request request, ClientInfo clientInfo, String fwdIpAddresses) throws LineappException {
		LoggerFactory.getLogger(LineappService.class).info(request.getClass().getName());
		LoggerFactory.getLogger(LineappService.class).info(Json.stringify(request));
		
    	try {
			if (request instanceof ExtendRequest) {
				return new Response<ExtendedAccessToken>(
						org.battlehack.lineapp.fb.FacebookAuthenticator.extend((ExtendRequest) request));
			} else if (request instanceof CreateRequest) {
				return new Response<org.battlehack.lineapp.api.Line>(Line.create(pm, (CreateRequest) request));
			} else if (request instanceof UpdateRequest) {
				return new Response<Events>(Line.update(pm, (UpdateRequest) request));
			} else if (request instanceof CreatePaymentRequest) {
				return new Response<Payment>(Pay.create(pm, (CreatePaymentRequest) request));
			} else if (request instanceof GetPaymentStatusRequest) {
				return new Response<PaymentStatus>(Pay.getStatus(pm, (GetPaymentStatusRequest) request));
			}
			
			throw new LineappException(new Error(Error.ERROR_INVALID_REQUEST, "invalid request: " +
					((request != null) ? request.getClass().getName() : "null")));
		} catch (DeadlineExceededException e) {
			LoggerFactory.getLogger(LineappService.class).error("Deadline exceeded", e);
			throw new LineappException(new Error(Error.ERROR_TIMEOUT, "request took too long"), e);
		} catch (RuntimeException e) {
			LoggerFactory.getLogger(LineappService.class).error("Internal error", e);
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "our dev team has been notified"), e);
		}
	}
	
	public static Response<?> execute(Request request, ClientInfo clientInfo, String fwdIpAddresses) {
    	final PersistenceManager pm = PMF.get().getPersistenceManager();
    	try {
    		return execute(pm, request, clientInfo, fwdIpAddresses);
    	} catch (LineappException e) {
    		LoggerFactory.getLogger(LineappService.class).warn(shorten(Json.stringify(request), MAX_LOGGED_JSON_LENGTH), e);
			return Response.fromException(e);
    	} finally {
    		pm.close();
    	}
	}
	
	private static final int MAX_LOGGED_JSON_LENGTH = 1024*20;
	
	private static String shorten(String str, int maxLength) {
		// +20 as sometimes the suffix just makes things longer
		if ((str == null) || (str.length() <= maxLength + 20)) {
			return str;
		}
		
		return str.substring(0, maxLength) + "... and " + (str.length() - maxLength) + " more characters.";
	}
}
