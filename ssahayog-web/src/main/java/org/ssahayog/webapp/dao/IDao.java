package org.ssahayog.webapp.dao;

import org.json.JSONObject;
import org.ssahayog.webapp.exception.ApplicationException;

public interface IDao {
	/**
	 * Generic method which, when given a query, returns result. THIS IS TO BE
	 * AVOIDED.
	 * @deprecated
	 */
	public JSONObject getQueryResult(final String sql)
			throws ApplicationException;
	
	public JSONObject getDonorDetails(final String givenName) throws ApplicationException;
	
	public JSONObject getVolunteerDetails(final String givenName) throws ApplicationException;
	
}
