package com.example.demo.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class PremiumServiceCompany implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String companyIdentificationNumber;
	private String companyName;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date registrationDate;
	private String fullAddress;
	@JsonProperty("isActive")
	private boolean active;

	public String getCompanyIdentificationNumber() {
		return companyIdentificationNumber;
	}

	public void setCompanyIdentificationNumber(String companyIdentificationNumber) {
		this.companyIdentificationNumber = companyIdentificationNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
