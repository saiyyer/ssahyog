package org.ssahayog.webapp.dao;

import org.json.JSONObject;
import org.ssahayog.webapp.exception.ApplicationException;

public interface IDao {
	public JSONObject getQueryResult(final String sql) throws ApplicationException;
}
