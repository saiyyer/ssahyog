package org.ssahayog.webapp.dao.vo;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class DonationDetails extends BasicVO {
	public static enum DepositType {
		CHEQUE, WIRE_TRANSFER, CASH, OTHER
	}

	private int pledgeId;
	private DepositType depositType;
	private String depositDetails;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date depositDate;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date encashedDate;
	private Double amount;
	private boolean verified;
	private Date verifiedDate;
	private String remarks;

	public DonationDetails() {
		setTransactionalTable(true);
	}
	
	public int getPledgeId() {
		return pledgeId;
	}

	public void setPledgeId(int pledgeId) {
		this.pledgeId = pledgeId;
	}

	public DepositType getDepositType() {
		return depositType;
	}

	public void setDepositType(DepositType depositType) {
		this.depositType = depositType;
	}

	public String getDepositDetails() {
		return depositDetails;
	}

	public void setDepositDetails(String depositDetails) {
		this.depositDetails = depositDetails;
	}

	public Date getDepositDate() {
		return depositDate;
	}

	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	public Date getEncashedDate() {
		return encashedDate;
	}

	public void setEncashedDate(Date encashedDate) {
		this.encashedDate = encashedDate;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public Date getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Date verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO DONATION_DETAILS(PLEDGEID, DEPOSIT_TYPE, DEPOSIT_DETAILS, DEPOSIT_DATE, ENCASHED_DATE, AMOUNT, VERIFIED, VERIFIED_DATE , REMARKS, IS_ACTIVE,MODIFIED_BY, MODIFIED_DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public String getUpdateSql() {
		throw new ApplicationException("Transaction Table should not be updated: "+toString());
	}

	@Override
	public String getRemoveSql() {
		return  "UPDATE DONATION_DETAILS SET IS_ACTIVE=? WHERE TRANSACTION_ID=?";
	}
	
	@Override
	public String getByIdSql() {
		return "SELECT * FROM DONATION_DETAILS WHERE TRANSACTION_ID = ?";
	}

	@Override
	public String getByNameSql() {
		throw new ApplicationException("getByName() is not valid query for DonationDetails..");
	}


	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {
		int index = 1;
		setValue(pstmt, index++, Types.INTEGER, getPledgeId());
		setValue(pstmt, index++, Types.VARCHAR, getDepositType().toString());
		setValue(pstmt, index++, Types.VARCHAR, getDepositDetails());
		setValue(pstmt, index++, Types.DATE, getDepositDate());
		setValue(pstmt, index++, Types.DATE, getEncashedDate());
		setValue(pstmt, index++, Types.INTEGER, getAmount());
		setValue(pstmt, index++, Types.CHAR, isVerified()?"Y":"N");
		setValue(pstmt, index++, Types.DATE, getVerifiedDate());
		setValue(pstmt, index++, Types.VARCHAR, getRemarks());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE, new java.util.Date(System.currentTimeMillis()));
		return index;
	}


	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("TRANSACTION_ID"));
			setPledgeId(rs.getInt("PLEDGEID"));
			setDepositType(DepositType.valueOf(rs.getString("DEPOSIT_TYPE")));
			setDepositDetails(rs.getString("DEPOSIT_DETAILS"));
			setDepositDate(rs.getDate("DEPOSIT_DATE"));
			setEncashedDate(rs.getDate("ENCASHED_DATE"));
			setAmount(rs.getDouble("AMOUNT"));
			setVerified("Y".equals(rs.getString("VERIFIED")));
			setVerifiedDate(rs.getDate("VERIFIED_DATE"));
			setRemarks(rs.getString("REMARKS"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getString("IS_ACTIVE")));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}
}
