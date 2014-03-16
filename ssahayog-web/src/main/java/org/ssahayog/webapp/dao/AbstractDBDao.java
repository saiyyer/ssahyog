package org.ssahayog.webapp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.ssahayog.webapp.dao.vo.BasicVO;
import org.ssahayog.webapp.exception.ApplicationException;

public abstract class AbstractDBDao implements IDao {
	
	protected DataSource ds;

	public AbstractDBDao(DataSource ds) throws ApplicationException {
		this.ds = ds;
	}

	public AbstractDBDao() throws ApplicationException {
		Context initContext;
		Context envContext;
		try {
			initContext = new InitialContext();
			envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/ssahayogdb");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ApplicationException("Error initializing Datasouce", e);
		}
	}	
	
	public <T extends BasicVO> int insert(T t) throws ApplicationException{
		return updateDB(t.getInsertSql(), t, false);
	}
	
	public <T extends BasicVO> int update(T t) throws ApplicationException{
		if(t.isTransactional()){
			remove(t);
			return insert(t);
		}
		return updateDB(t.getUpdateSql(), t, false);
	}
	
	public <T extends BasicVO> int remove(T t) throws ApplicationException{
		return updateDB(t.getRemoveSql(), t, true);
	}
		
	@Override
	public <T extends BasicVO> T getById(final int id, Class<T> voClass) throws ApplicationException {
		try {
			final T vo = voClass.newInstance();
			return populateVO( voClass, new AbstractQueryHelper<T>() {
				@Override
				public String getSQL() {
					return vo.getByIdSql();
				}
				@Override
				public void prepare(PreparedStatement pstmt) throws SQLException {
					vo.prepareSelectById(pstmt, id);
				}
			}).get(0);
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new ApplicationException("Some problem...", e);
		}
	}

	@Override
	public <T extends BasicVO> List<T>  getByName(final String inputName, Class<T> voClass)
			throws ApplicationException {

		try {
			final T vo = voClass.newInstance();
			return populateVO(voClass,new AbstractQueryHelper<T>() {
				@Override
				public String getSQL() {
					return vo.getByNameSql();
				}
				@Override
				public void prepare(PreparedStatement pstmt) throws SQLException {
					vo.prepareSelectByName(pstmt, inputName);
				}
				
			});
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
			throw new ApplicationException("Some problem...", e);
		}
	
	}

	private <T extends BasicVO> List<T> populateVO(Class<T> voClass,final QueryHelper<T> querier ) throws ApplicationException{
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(querier.getSQL());
			querier.prepare(pstmt);
			rs = pstmt.executeQuery();
			return querier.constructAndReturn(voClass,rs);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Error updating DB", e);
		} finally {
			closeAll(rs,pstmt, conn);
		}
	}
	
	protected <T extends BasicVO> int updateDB(final String sql, final T t, boolean remove) throws ApplicationException{
		Connection conn = null;
		PreparedStatement pstmt = null;
		try {
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(sql);
			t.prepare(pstmt, remove);
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Error updating DB", e);
		} finally {
			closeAll(pstmt,conn);
		}
	}
	
	protected final void closeAll(final Statement stmt, final Connection conn){
		close(stmt);
		close(conn);
	}
	protected final void closeAll(final ResultSet rs, final Statement stmt, final Connection conn){
		close(rs);
		close(stmt);
		close(conn);
	}

	protected final void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	protected final void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	protected final void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}
}
