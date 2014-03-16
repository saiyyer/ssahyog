package org.ssahayog.webapp.dao.vo;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class Event extends BasicVO {
	private String eventName;
	private String eventDescription;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date eventDate;
	private String remarks;
	
	public String getEventName() {
		return eventName;
	}
	public void setEventName(String eventName) {
		this.eventName = eventName;
	}
	public String getEventDescription() {
		return eventDescription;
	}
	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}
	public Date getEventDate() {
		return eventDate;
	}
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Override
	public String getInsertSql() {
		return "INSERT INTO EVENT(EVENT_NAME,EVENT_DESCRIPTION,EVENT_DATE,REMARKS,MODIFIED_BY,MODIFIED_DATE) VALUES(?,?,?,?,?,?)";
	}
	@Override
	public String getUpdateSql() {
		return "UPDATE EVENT SET EVENT_NAME=?, EVENT_DESCRIPTION=?, EVENT_DATE=?, REMARKS=?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE EVENT_ID=?";
	}
	@Override
	public String getRemoveSql() {
		throw new ApplicationException("Cannot delete/deactivate an Event"); 
	}
	@Override
	public String getByIdSql() {
		return "SELECT * FROM EVENT WHERE EVENT_ID=?";
	}
	@Override
	public String getByNameSql() {
		return "SELECT * FROM EVENT WHERE EVENT_NAME like ?";
	}
	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {
		int index = 1;
		setValue(pstmt, index++, Types.VARCHAR, getEventName());
		setValue(pstmt, index++, Types.VARCHAR, getEventDescription());
		setValue(pstmt, index++, Types.DATE, getEventDate());
		setValue(pstmt, index++, Types.VARCHAR, getRemarks());
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE, new java.util.Date(System.currentTimeMillis()));
			return index;
	}
	@Override
	public void populate(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
}
