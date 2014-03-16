package org.ssahayog.webapp.dao.vo;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public class DonorPledge extends BasicVO {

	public static enum DonationFrequency {
		ANUALLY, HALF_YEARLY, QUARTERLY, BI_MONTHLY, MONTHLY, CUSTOM
	}

	private int donorId;
	private int beneficiaryId;
	private int formId;
	private DonationFrequency frequency;
	private Double amount;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date dateFrom;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date dateUntil;

	public int getDonorId() {
		return donorId;
	}

	public void setDonorId(int donorId) {
		this.donorId = donorId;
	}

	public int getBeneficiaryId() {
		return beneficiaryId;
	}

	public void setBeneficiaryId(int beneficiaryId) {
		this.beneficiaryId = beneficiaryId;
	}

	public int getFormId() {
		return formId;
	}

	public void setFormId(int formId) {
		this.formId = formId;
	}

	public DonationFrequency getFrequency() {
		return frequency;
	}

	public void setFrequency(DonationFrequency frequency) {
		this.frequency = frequency;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateUntil() {
		return dateUntil;
	}

	public void setDateUntil(Date dateUntil) {
		this.dateUntil = dateUntil;
	}

	@Override
	public String getInsertSql() {
		return "INSERT INTO DONOR_PLEDGE(DONORID,BENEFICIARYID,FORMID,DONATION_FREQUENCY,AMOUNT,DATE_FROM,DATE_UNTIL,IS_ACTIVE,MODIFIED_BY,MODIFIED_DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
	}

	@Override
	public String getUpdateSql() {
		return "UPDATE DONOR_PLEDGE SET DONORID=?, BENEFICIARYID=?, FORMID=?, DONATION_FREQUENCY=?, AMOUNT=?, DATE_FROM=?, DATE_UNTIL=?, IS_ACTIVE=?, MODIFIED_BY=?, MODIFIED_DATE=? WHERE PLEDGEID=?";
	}

	@Override
	public String getRemoveSql() {
		return "UPDATE DONOR_PLEDGE SET IS_ACTIVE=? WHERE PLEDGEID=?";
	}
	@Override
	public String getByIdSql() {
		return "SELECT * FROM DONOR_PLEDGE WHERE PLEDGEID=?";
	}

	@Override
	public String getByNameSql() {
		throw new ApplicationException("getByName not a valid query for DonorPledge");
	}

	
	@Override
	public int prepareInsert(PreparedStatement pstmt)
			throws SQLException {

		int index = 1;
		setValue(pstmt, index++, Types.INTEGER, getDonorId());
		setValue(pstmt, index++, Types.INTEGER, getBeneficiaryId());
		setValue(pstmt, index++, Types.VARCHAR, getFrequency().toString());
		setValue(pstmt, index++, Types.DOUBLE, getAmount());
		setValue(pstmt, index++, Types.DATE, getDateFrom());
		setValue(pstmt, index++, Types.DATE, getDateUntil());
		setValue(pstmt, index++, Types.VARCHAR, ACTIVE_FLAG_YES);
		setValue(pstmt, index++, Types.VARCHAR, getModifiedBy());
		setValue(pstmt, index++, Types.DATE, new java.util.Date(System.currentTimeMillis()));
			
			return index;
	}


	@Override
	public void populate(ResultSet rs) throws SQLException {
		if(rs != null){
			setId(rs.getInt("PLEDGEID"));
			setDonorId(rs.getInt("DONORID"));
			setBeneficiaryId(rs.getInt("BENEFICIARYID"));
			setFormId(rs.getInt("FORMID"));
			setFrequency(DonationFrequency.valueOf(rs.getString("DONATION_FREQUENCY")));
			setAmount(rs.getDouble("AMOUNT"));
			setDateFrom(rs.getDate("DATE_FROM"));
			setDateUntil(rs.getDate("DATE_UNTIL"));
			setActive(ACTIVE_FLAG_YES.equals(rs.getString("IS_ACTIVE")));
			setModifiedBy(rs.getString("MODIFIED_BY"));
			setModifiedDate(rs.getDate("MODIFIED_DATE"));
		}
	}

}
