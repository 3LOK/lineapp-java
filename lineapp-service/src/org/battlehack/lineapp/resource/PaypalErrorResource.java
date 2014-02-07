package org.battlehack.lineapp.resource;

import javax.jdo.PersistenceManager;

import org.battlehack.lineapp.persistent.PMF;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public class PaypalErrorResource extends ServerResource {
	@Get("html")
	public String doGet() {
    	final String payInternalId = (String) getRequestAttributes().get("payInternalId");
		
		final PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			final org.battlehack.lineapp.persistent.PaymentStatus persistPaymentStatus =
					org.battlehack.lineapp.persistent.PaymentStatus.byInternalIdNoThrow(pm, payInternalId);
			if (persistPaymentStatus != null) {
				persistPaymentStatus.setStatus(org.battlehack.lineapp.api.PaymentStatus.STATUS_ERROR);
			}
		} finally {
			pm.close();
		}
		
		return "<html><script>window.close();</script></html>";
	}
}
