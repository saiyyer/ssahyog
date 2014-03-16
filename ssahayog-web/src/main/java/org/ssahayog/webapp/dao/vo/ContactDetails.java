package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class ContactDetails extends BasicVO {
	private int pledgeId;
	private int volunteerId;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd HHmmss.SSS", timezone = "IST")
	private Date date;
	private String remarks;

	public int getPledgeId() {
		return pledgeId;
	}

	public void setPledgeId(int pledgeId) {
		this.pledgeId = pledgeId;
	}

	public int getVolunteerId() {
		return volunteerId;
	}

	public void setVolunteerId(int volunteerId) {
		this.volunteerId = volunteerId;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO CONTACT_DETAILS (PLEDGEID,VOLUNTEERID,DATE_CONTACTED,REMARKS,MODIFIED_BY,MODIFIED_DATE) VALUES(?,?,?,?,?,?)";
	}

	@Override
	public String getUpdateSql() {
		return "UPDATE CONTACT_DETAILS SET PLEDGEID=?, VOLUNTEERID=?, DATE_CONTACTED=?, REMARKS=?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE ID=?";
	}

	@Override
	public String getRemoveSql() {
		throw new ApplicationException(
				"Cannot deactivate/remove a Contact Detail record: "
						+ toString());
	}

	@Override
	public String getByIdSql() {
		return "SELECT * FROM CONTACT_DETAILS WHERE ID=?";
	}
	
	@Override
	public String getByNameSql() {
		throw new ApplicationException("GetByName() not valid Query for Contact details"); 
	}
	@Override
	public int prepareInsert(PreparedStatement pstmt) throws SQLException {

		int index = 1;
		setValue(pstmt, index++, Types.INTEGER, getPledgeId());
		setValue(pstmt, index++, Types.INTEGER, getVolunteerId());
		setValue(pstmt, index++, Types.DATE, getDate());
		setValue(pstmt, index++, Types.VARCHAR, getRemarks());
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE,
				new java.util.Date(System.currentTimeMillis()));
		return index;

	}


	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("ID"));
			setPledgeId(rs.getInt("PLEDGEID"));
			setVolunteerId(rs.getInt("VOLUNTEERID"));
			setDate(rs.getDate("DATE_CONTACTED"));
			setRemarks(rs.getString("REMARKS"));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}
}
