package com.example.demo.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class ServiceCompany {

	@Id
	private String cin;
	private String name;
	private Date registrationDate;
	private String address;
	private String fullAddress;
	private boolean isActive;
	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "service_providers", joinColumns = @JoinColumn(name = "company_id"))
	@Column(name = "provider")
	private Set<ServiceProvider> providers = new HashSet<>();

	public String getCin() {
		return cin;
	}

	public void setCin(String cin) {
		this.cin = cin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Set<ServiceProvider> getProviders() {
		return providers;
	}

	public void setProviders(Set<ServiceProvider> providers) {
		this.providers = providers;
	}

	public void addProvider(ServiceProvider provider) {
		providers.add(provider);
	}

}
