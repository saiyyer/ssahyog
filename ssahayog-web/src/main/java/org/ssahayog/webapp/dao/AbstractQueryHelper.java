package org.ssahayog.webapp.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.ssahayog.webapp.dao.vo.BasicVO;
import org.ssahayog.webapp.exception.ApplicationException;

public  abstract class  AbstractQueryHelper<T extends BasicVO> implements QueryHelper<T>{
	
	@Override
	public  List<T> constructAndReturn(Class<T> voClass,
			ResultSet rs) throws SQLException {
		List<T> vos = new ArrayList<T>();
		T vo;
		while(rs.next()){
			try {
			vo = voClass.newInstance();
			vo.populate(rs);
			vos.add(vo);
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				throw new ApplicationException("Problem in VO construction..", e);
			}
		}
		return vos;
	}
}
