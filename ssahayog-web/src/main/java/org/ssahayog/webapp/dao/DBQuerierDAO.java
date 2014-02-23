package org.ssahayog.webapp.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.json.JSONObject;
import org.ssahayog.webapp.exception.ApplicationException;
import org.ssahayog.webapp.json.JsonProcessor;

public class DBQuerierDAO implements IDao {

	private static final String SEMI_COLON = ";";
	private static final String SQL_COMMENT = "--";

	private static final String[] DDL_COMMANDS = { "DROP", "TRUNCATE", "DELETE" };

	private DataSource ds;

	public DBQuerierDAO(DataSource ds) throws ApplicationException {
		this.ds = ds;
	}

	public DBQuerierDAO() throws ApplicationException {
		Context initContext;
		Context envContext;
		try {
			initContext = new InitialContext();
			envContext = (Context) initContext.lookup("java:/comp/env");
			ds = (DataSource) envContext.lookup("jdbc/ssahyogdb");
		} catch (NamingException e) {
			e.printStackTrace();
			throw new ApplicationException("Error initializing Datasouce", e);
		}
	}

	private static final boolean isValidSelectQuery(final String sql)
			throws ApplicationException {

		if (sql == null || sql.contains(SEMI_COLON)
				|| sql.contains(SQL_COMMENT)) {
			throw new ApplicationException(
					"SQL is NULL or sql contains Semi-colon, comment. Reframe the query");
		}
		for (String DdlCommand : DDL_COMMANDS) {
			if (sql.indexOf(DdlCommand) > 0) {
				throw new ApplicationException(
						"SQL contains DELETE/DROP/TRUNCATE command. Reframe the query");
			}
		}
		return true;
	}

	private void prepareStatement(final PreparedStatement pstmt, Object...parameters) throws SQLException{
		if(parameters != null && parameters.length>0){
			int position = 0;
			for(Object param : parameters){
				++position;
				if(param instanceof Boolean){
					pstmt.setBoolean(position, (Boolean) param);
				} else if(param instanceof Integer){
					pstmt.setInt(position, (Integer) param);					
				} else if(param instanceof Double){
					pstmt.setDouble(position, (Double) param);					
				} else if(param instanceof Float){
					pstmt.setFloat(position, (Float) param);					
				} else if(param instanceof Date){
					pstmt.setDate(position, (Date) param);					
				} else if(param instanceof String){
					// TODO: Is this relevant?
					pstmt.setString(position, "%"+((String) param).toUpperCase()+"%");					
				}
			}
		}
	}

	public JSONObject getQueryResult(String sql, Object... parameters)
			throws ApplicationException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		if (isValidSelectQuery(sql)) {
			try {
				conn = ds.getConnection();
				pstmt = conn.prepareStatement(sql);
				prepareStatement(pstmt,parameters);
				rs = pstmt.executeQuery();
				return JsonProcessor.createResultSet(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				return JsonProcessor.createErrorMessage(new ApplicationException(
						"Error while executing query -  " + sql, e));
			} finally {
				close(rs);
				close(pstmt);
				close(conn);
			}
		}
		return JsonProcessor.createEmptyMessage();
	}

	public JSONObject getQueryResult(String sql) throws ApplicationException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		if (isValidSelectQuery(sql)) {
			try {
				conn = ds.getConnection();
				stmt = conn.createStatement();
				rs = stmt.executeQuery(sql);
				return JsonProcessor.createResultSet(rs);
			} catch (SQLException e) {
				e.printStackTrace();
				JsonProcessor.createErrorMessage(new ApplicationException(
						"Error while executing query -  " + sql, e));
			} finally {
				close(rs);
				close(stmt);
				close(conn);
			}
		}
		return JsonProcessor.createEmptyMessage();
	}

	public JSONObject getDonorDetails(String givenName)
			throws ApplicationException {
		return JsonProcessor.createEmptyMessage();
	}

	public JSONObject getVolunteerDetails(String givenName)
			throws ApplicationException {
		final String SELECT_QUERY = "SELECT VOLUNTEERID as VolunteerID, VOLUNTEER_NAME as VolunteerName, RES_ADDRESS as ResidentialAddress FROM VOLUNTEER_DETAILS WHERE UPPER(VOLUNTEER_NAME) LIKE ?";
		if(givenName == null){
			return getQueryResult(SELECT_QUERY, "");
		}
		return getQueryResult(SELECT_QUERY, givenName);
	}

	private final void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
			}
		}
	}

	private final void close(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
			}
		}
	}

	private final void close(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
			}
		}
	}

}
