package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Beneficiary extends Entity {

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO BENEFICIARY(NGONAME,ADDRESS,PHONE,EMAIL,CITY,STATE,PIN,IS_ACTIVE,ACTIVE_SINCE,MODIFIED_BY,MODIFIED_DATE)VALUES(?,?,?,?,?,?,?,?,?,?,?)";

	}

	@Override
	public String getUpdateSql() {
		return "UPDATE BENEFICIARY SET NGONAME=?, ADDRESS=?, PHONE=?, EMAIL=?, CITY=?, STATE=?, PIN=?, IS_ACTIVE=?, ACTIVE_SINCE=?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE BENEFICIARYID = ?";
	}

	@Override
	public String getRemoveSql() {
		return "UPDATE BENEFICIARY SET ISACTIVE=? WHERE BENEFICIARYID=?";
	}

	@Override
	public String getByIdSql() {
		return "SELECT * FROM BENEFICIARY WHERE ID=?";
	}
	
	@Override
	public String getByNameSql() {
		return "SELECT * FROM BENEFICIARY WHERE UPPER(NGONAME) like ?";
	}
	
	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {
		int index = 1;
		setValue(pstmt, index++, Types.VARCHAR, getName());
		setValue(pstmt, index++, Types.VARCHAR, getOfficeAddress());
		setValue(pstmt, index++, Types.VARCHAR, getOfficePhone());
		setValue(pstmt, index++, Types.VARCHAR, getEmail());
		setValue(pstmt, index++, Types.VARCHAR, getCity());
		setValue(pstmt, index++, Types.VARCHAR, getState());
		setValue(pstmt, index++, Types.INTEGER, getPin());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.DATE, getActiveSince());
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE,
				new java.util.Date(System.currentTimeMillis()));
		return index;
	}

	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("BENEFICIARYID"));
			setName(rs.getString("NGONAME"));
			setOfficeAddress(rs.getString("ADDRESS"));
			setOfficePhone(rs.getString("PHONE"));
			setEmail(rs.getString("EMAIL"));
			setCity(rs.getString("CITY"));
			setState(rs.getString("STATE"));
			setPin(rs.getInt("PIN"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getString("ISACTIVE")));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}

}
