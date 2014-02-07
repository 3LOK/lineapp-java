package org.battlehack.lineapp.persistent;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.LineappException;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PaymentStatus {
	public PaymentStatus(String payInternalId, String payKey) {
		this.payInternalId = payInternalId;
		this.payKey = payKey;
		this.status = org.battlehack.lineapp.api.PaymentStatus.STATUS_PENDING;
	}
	
	public org.battlehack.lineapp.api.PaymentStatus get() {
		return new org.battlehack.lineapp.api.PaymentStatus(payKey, status);
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public static PaymentStatus byInternalIdNoThrow(PersistenceManager pm, String internalId) {
		try {
			return pm.getObjectById(PaymentStatus.class, internalId);
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	public static PaymentStatus byIdNoThrow(PersistenceManager pm, String payInternalId) {
		try {
			return pm.getObjectById(PaymentStatus.class, payInternalId);
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	public static String queryStatus(PersistenceManager pm, String payKey) throws LineappException {
		final Query query = pm.newQuery(PaymentStatus.class);
		
		query.setFilter("payKey == payKeyParam");
		query.declareParameters("String payKeyParam");
		
		@SuppressWarnings("unchecked")
		final List<PaymentStatus> persistPaymentStatus = (List<PaymentStatus>) query.execute(payKey);
		
		if (persistPaymentStatus.isEmpty()) {
			throw new LineappException(new Error(Error.ERROR_NOT_FOUND, "unknown payKey: " + payKey));
		}
		
		if (persistPaymentStatus.size() > 1) {
			throw new LineappException(new Error(Error.ERROR_INTERNAL, "found " + persistPaymentStatus.size() + " statuses"));
		}
		
		return persistPaymentStatus.get(0).status;
	}
	
	@PrimaryKey
    @Persistent
    private String payInternalId;
	
    @Persistent
    private String payKey;
    
    @Persistent
    private String status;
}
