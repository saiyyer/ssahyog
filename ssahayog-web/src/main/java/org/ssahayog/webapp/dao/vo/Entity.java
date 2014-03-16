package org.ssahayog.webapp.dao.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

public abstract class Entity extends BasicVO {
	
	private String name;
	private String officeAddress;
	private String officePhone;
	private String city;
	private String state;
	private String email;
	private int pin;
	@JsonFormat(shape=Shape.STRING, pattern="yyyyMMdd HHmmss.SSS", timezone="IST")
	private Date activeSince;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOfficeAddress() {
		return officeAddress;
	}

	public void setOfficeAddress(String address) {
		this.officeAddress = address;
	}

	public void setOfficePhone(String officePhone) {
		this.officePhone = officePhone;
	}

	public String getOfficePhone() {
		return officePhone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getPin() {
		return pin;
	}

	public void setPin(int pin) {
		this.pin = pin;
	}

	public Date getActiveSince() {
		return activeSince;
	}

	public void setActiveSince(Date activeSince) {
		this.activeSince = activeSince;
	}
}
