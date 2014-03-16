package org.ssahayog.webapp.dao.vo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class Form extends BasicVO {
	private String formName;
	private String formDescription;

	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	public String getFormDescription() {
		return formDescription;
	}

	public void setFormDescription(String formDescription) {
		this.formDescription = formDescription;
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO FORM(FORM_NAME,FORM_DESC,IS_ACTIVE,MODIFIED_BY,MODIFIED_DATE)VALUES(?,?,?,?,?)"; 
	}

	@Override
	public String getUpdateSql() {
		return "UPDATE FORM SET FORM_NAME=?, FORM_DESC=?, IS_ACTIVE=?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE FORMID=?"; 
	}

	@Override
	public String getRemoveSql() {
		return "UPDATE FORM SET IS_ACTIVE=? WHERE FORMID=?"; 
	}

	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {
		int index = 1;
		setValue(pstmt, index++, Types.VARCHAR, getFormName());
		setValue(pstmt, index++, Types.VARCHAR, getFormDescription());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.VARCHAR, new java.util.Date(System.currentTimeMillis()));
		return index;
	}

	@Override
	public String getByIdSql() {
		return "SELECT * FROM FORM WHERE FORMID = ?";
	}

	@Override
	public String getByNameSql() {
		return "SELECT * FROM FORM WHERE upper(FORM_NAME) like ?";
	}

	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("FORMID"));
			setFormName(rs.getString("FORM_NAME"));
			setFormDescription(rs.getString("FORM_DESC"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getInt("IS_ACTIVE")));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}
}
