package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Donor extends Entity {
	private int referedByVolunteer;
	private String residentialAddress;
	private String ResidentialPhone;
	private String mobilePhone;
	
	
	public int getReferedByVolunteer() {
		return referedByVolunteer;
	}

	public void setReferedByVolunteer(int referedByVolunteer) {
		this.referedByVolunteer = referedByVolunteer;
	}
	
	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
	
	public String getResidentialAddress() {
		return residentialAddress;
	}
	
	public void setResidentialPhone(String residentialPhone) {
		ResidentialPhone = residentialPhone;
	}
	
	public String getResidentialPhone() {
		return ResidentialPhone;
	}
	
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}
	
	public String getMobilePhone() {
		return mobilePhone;
	}

	@Override
	public String getInsertSql() {
		return  "INSERT INTO DONOR_DETAILS(DONOR_NAME,RES_ADDRESS,OFF_ADDRESS,RES_PHONE,OFF_PHONE,MOBILE_PHONE,EMAIL,CITY,STATE,PIN,REFERRAL,IS_ACTIVE, ACTIVE_SINCE,MODIFIED_BY,MODIFIED_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public String getUpdateSql() {
		return  "UPDATE DONOR_DETAILS SET DONOR_NAME=?, RES_ADDRESS=?, OFF_ADDRESS=?, RES_PHONE=?, OFF_PHONE=?, MOBILE_PHONE=?, EMAIL=?, CITY=?, STATE=?, PIN=?, REFERRAL=?, IS_ACTIVE=?, ACTIVE_SINCE=?,MODIFIED_BY=?, MODIFIED_DATE=? WHERE DONOR_ID=?";
	}

	@Override
	public String getRemoveSql() {
		return "UPDATE DONOR_DETAILS SET IS_ACTIVE=? WHERE DONOR_ID=?";
	}
	
	@Override
	public String getByIdSql() {
		return "SELECT * FROM DONOR_DETAILS WHERE DONORID=?";
	}
	
	@Override
	public String getByNameSql() {
		return "SELECT * FROM DONOR_DETAILS WHERE DONOR_NAME=?";
	}

	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {

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
		setValue(pstmt, index++, Types.INTEGER, getReferedByVolunteer());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.DATE, getActiveSince());
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE, new java.util.Date(System.currentTimeMillis()));
		return index;
	}

	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("DONORID"));
			setName(rs.getString("DONOR_NAME"));
			setResidentialAddress(rs.getString("RES_ADDRESS"));
			setOfficeAddress(rs.getString("OFF_ADDRESS"));
			setResidentialPhone(rs.getString("RES_PHONE"));
			setOfficePhone(rs.getString("OFF_PHONE"));
			setMobilePhone(rs.getString("MOBILE_PHONE"));
			setEmail(rs.getString("EMAIL"));
			setCity(rs.getString("CITY"));
			setState(rs.getString("STATE"));
			setPin(rs.getInt("PIN"));
			setReferedByVolunteer(rs.getInt("REFERRAL"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getString("IS_ACTIVE")));
			setActiveSince(rs.getDate("ACTIVE_SINCE"));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}
}
