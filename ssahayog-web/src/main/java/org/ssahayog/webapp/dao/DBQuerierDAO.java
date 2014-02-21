package org.ssahayog.webapp.dao;

import java.sql.Connection;
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

	public boolean isValidSelectQuery(final String sql)
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

				throw new ApplicationException(
						"Error while executing query -  " + sql, e);
			} finally {
				close(rs);
				close(stmt);
				close(conn);
			}
		}
		return null;
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
