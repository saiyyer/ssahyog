package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class BasicVO {

	protected static final String ACTIVE_FLAG_YES = "Y";
	protected static final String ACTIVE_FLAG_NO = "N";

	protected int id;

	protected boolean isActive = true;
	protected String modifiedBy;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd HHmmss.SSS", timezone = "IST")
	protected Date modifiedDate;
	@JsonIgnore
	protected boolean transactional = false;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@JsonIgnore
	public boolean isTransactional() {
		return transactional;
	}

	public void setTransactionalTable(boolean transactional) {
		this.transactional = transactional;
	}

	@JsonIgnore
	protected static final void setValue(PreparedStatement pstmt, int index,
			int type, Object value) throws SQLException {
		if (value == null) {
			pstmt.setNull(index, type);
			return;
		}
		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			pstmt.setString(index, (String) value);
			break;
		case Types.SMALLINT:
		case Types.INTEGER:
		case Types.NUMERIC:
			pstmt.setInt(index, (int) value);
			break;
		case Types.FLOAT:
			pstmt.setFloat(index, (float) value);
			break;
		case Types.DOUBLE:
			pstmt.setDouble(index, (double) value);
			break;
		case Types.DATE:
			pstmt.setDate(index,
					new java.sql.Date(((java.util.Date) value).getTime()));
			break;
		case Types.TIME:
			pstmt.setTime(index,
					new java.sql.Time(((java.util.Date) value).getTime()));
			break;
		case Types.TIMESTAMP:
			pstmt.setTimestamp(index, (java.sql.Timestamp) value);
			break;
		default:
			throw new ApplicationException("Unknown type identified at index: "
					+ index + " value: " + value);
		}
	}

	@JsonIgnore
	public abstract String getInsertSql();

	@JsonIgnore
	public abstract String getUpdateSql();

	@JsonIgnore
	public abstract String getRemoveSql();

	@JsonIgnore
	public abstract String getByIdSql();

	@JsonIgnore
	public abstract String getByNameSql();

	@JsonIgnore
	public abstract void populate(final ResultSet rs) throws SQLException;

	@JsonIgnore
	public void prepareSelectByName(final PreparedStatement pstmt, String name)
			throws SQLException {
		if (name == null || "".equals(name)) {
			throw new ApplicationException("Input value not supplied..");
		}
		pstmt.setString(1, "%" + name.toUpperCase() + "%");
	}

	@JsonIgnore
	public void prepareSelectById(final PreparedStatement pstmt, int id)
			throws SQLException {
		pstmt.setInt(1, id);
	}

	@JsonIgnore
	public void prepare(final PreparedStatement pstmt, boolean isRemove)
			throws SQLException {
		if (isRemove) {
			prepareRemove(pstmt);
		} else {
			int lastIndex = prepareInsert(pstmt);
			if (getId() != 0) {
				pstmt.setInt(lastIndex, getId());
			}
		}
	}
	
	@JsonIgnore
	public abstract int prepareInsert(PreparedStatement pstmt)
			throws SQLException;

	@JsonIgnore
	public void prepareRemove(PreparedStatement pstmt) throws SQLException {
		int index = 1;
		pstmt.setString(index++, ACTIVE_FLAG_NO);
		pstmt.setInt(index++, getId());
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
