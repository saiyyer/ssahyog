package org.ssahayog.webapp.dao;

import java.util.List;

import org.json.JSONObject;
import org.ssahayog.webapp.dao.vo.BasicVO;
import org.ssahayog.webapp.exception.ApplicationException;

public  interface IDao {
	/**
	 * Generic method which, when given a query, returns result. THIS IS TO BE
	 * AVOIDED.
	 * @deprecated
	 */
	public JSONObject getQueryResult(final String sql)
			throws ApplicationException;
	
	public JSONObject getDonorDetails(final String givenName) throws ApplicationException;
	
	public JSONObject getVolunteerDetails(final String givenName) throws ApplicationException;

	public <T extends BasicVO> int insert(T t) throws ApplicationException; 
	public <T extends BasicVO> int update(T t) throws ApplicationException; 
	public <T extends BasicVO> int remove(T t) throws ApplicationException; 
	public <T extends BasicVO> T getById(int id, Class<T> voClass) throws ApplicationException; 
	public <T extends BasicVO> List<T> getByName(String inputName, Class<T> voClass) throws ApplicationException; 
	
}
