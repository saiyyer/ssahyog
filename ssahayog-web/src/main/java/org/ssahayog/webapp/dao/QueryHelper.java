package org.ssahayog.webapp.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.ssahayog.webapp.dao.vo.BasicVO;

public  interface  QueryHelper<T extends BasicVO>   {
	String getSQL();
	void prepare(PreparedStatement pstmt) throws SQLException;
	List<T>  constructAndReturn(Class<T> voClass, final ResultSet rs) throws SQLException;
}
