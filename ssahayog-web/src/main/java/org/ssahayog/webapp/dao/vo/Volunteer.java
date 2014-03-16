package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Volunteer extends Entity {
	private String residentialAddress;
	private String residentialPhone;
	private String mobilePhone;
	private String bestTimeToContact;
	private String timeCommitment;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyyMMdd HHmmss.SSS", timezone = "IST")
	private Date leavingDate;

	@Override
	@JsonProperty(value="volunteerName")
	public String getName() {
		// TODO Auto-generated method stub
		return super.getName();
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}

	public void setResidentialPhone(String residentialPhone) {
		this.residentialPhone = residentialPhone;
	}

	public String getResidentialPhone() {
		return residentialPhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getBestTimeToContact() {
		return bestTimeToContact;
	}

	public void setBestTimeToContact(String bestTimeToContact) {
		this.bestTimeToContact = bestTimeToContact;
	}

	public String getTimeCommitment() {
		return timeCommitment;
	}

	public void setTimeCommitment(String timeCommitment) {
		this.timeCommitment = timeCommitment;
	}

	public Date getLeavingDate() {
		return leavingDate;
	}

	public void setLeavingDate(Date leavingDate) {
		this.leavingDate = leavingDate;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO VOLUNTEER_DETAILS (VOLUNTEER_NAME,RES_ADDRESS,OFF_ADDRESS,RES_PHONE,OFF_PHONE,MOBILE_PHONE,EMAIL,CITY,STATE,PIN,BEST_TIME_TO_CONTACT,TIME_COMMITMENT,IS_ACTIVE,ACTIVE_SINCE,LEAVING_DATE,MODIFIED_BY,MODIFIED_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
	}

	@Override
	public String getUpdateSql() {
		return "UPDATE VOLUNTEER_DETAILS SET VOLUNTEER_NAME=?, RES_ADDRESS = ?, OFF_ADDRESS = ?, RES_PHONE = ?, OFF_PHONE = ?, MOBILE_PHONE=?, EMAIL=?, CITY=?, STATE=?, PIN=?, BEST_TIME_TO_CONTACT =?, TIME_COMMITMENT=?, IS_ACTIVE=?, ACTIVE_SINCE=?, LEAVING_DATE =?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE VOLUNTEERID = ?";
	}

	@Override
	public String getRemoveSql() {
		return "UPDATE VOLUNTEER_DETAILS SET IS_ACTIVE=? WHERE VOLUNTEERID=?";
	}

	@Override
	public String getByIdSql() {
		return "SELECT * FROM VOLUNTEER_DETAILS WHERE VOLUNTEERID = ? ";
	}

	@Override
	public String getByNameSql() {
		return "SELECT * FROM VOLUNTEER_DETAILS WHERE UPPER(VOLUNTEER_NAME) like ? ";
	}

	@Override
	public int prepareInsert(PreparedStatement pstmt) throws SQLException {
		int index = 1;
		setValue(pstmt, index++, Types.VARCHAR, getName());
		setValue(pstmt, index++, Types.VARCHAR, getResidentialAddress());
		setValue(pstmt, index++, Types.VARCHAR, getOfficeAddress());
		setValue(pstmt, index++, Types.VARCHAR, getResidentialPhone());
		setValue(pstmt, index++, Types.VARCHAR, getOfficePhone());
		setValue(pstmt, index++, Types.VARCHAR, getMobilePhone());
		setValue(pstmt, index++, Types.VARCHAR, getEmail());
		setValue(pstmt, index++, Types.VARCHAR, getCity());
		setValue(pstmt, index++, Types.VARCHAR, getState());
		setValue(pstmt, index++, Types.INTEGER, getPin());
		setValue(pstmt, index++, Types.VARCHAR, getBestTimeToContact());
		setValue(pstmt, index++, Types.VARCHAR, getTimeCommitment());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.DATE, getActiveSince());
		setValue(pstmt, index++, Types.DATE, getLeavingDate());
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE,
				new java.util.Date(System.currentTimeMillis()));
		return index;
	}

	@Override
	public void populate(ResultSet rs) throws SQLException {
		if (rs != null) {
			setId(rs.getInt("VOLUNTEERID"));
			setName(rs.getString("VOLUNTEER_NAME"));
			setResidentialAddress(rs.getString("RES_ADDRESS"));
			setOfficeAddress(rs.getString("OFF_ADDRESS"));
			setResidentialPhone(rs.getString("RES_PHONE"));
			setOfficePhone(rs.getString("OFF_PHONE"));
			setMobilePhone(rs.getString("MOBILE_PHONE"));
			setEmail(rs.getString("EMAIL"));
			setCity(rs.getString("CITY"));
			setState(rs.getString("STATE"));
			setPin(rs.getInt("PIN"));
//			set(rs.getInt("STILL_INTERESTED"));
			setBestTimeToContact(rs.getString("BEST_TIME_TO_CONTACT"));
			setTimeCommitment(rs.getString("TIME_COMMITMENT"));
			setActiveSince(rs.getDate("ACTIVE_SINCE"));
			setLeavingDate(rs.getDate("LEAVING_DATE"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getString("IS_ACTIVE")));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}
}
