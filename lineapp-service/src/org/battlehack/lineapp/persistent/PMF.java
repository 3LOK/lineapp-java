package org.battlehack.lineapp.persistent;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

public final class PMF {
	/** @see http://code.google.com/appengine/docs/python/datastore/gqlreference.html */
	public static final int MAX_QUERIES_PER_GQL = 30;
	
	private PMF() {}

	private static final PersistenceManagerFactory pmfInstance =
		JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManagerFactory get() {
		return pmfInstance;
	}
}
