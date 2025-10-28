package com.example.demo.entity.dto;

import java.io.Serializable;
import java.util.List;

import com.example.demo.entity.ServiceProvider;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class ResponseDto implements DataTransferObject {

	private static final long serialVersionUID = 1L;

	private String verificationId;

	private String query;

	private Serializable result;

	private List<Serializable> otherResults;

	@JsonIgnore
	private ServiceProvider source;

	public String getVerificationId() {
		return verificationId;
	}

	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public Serializable getResult() {
		return result;
	}

	public void setResult(Serializable result) {
		this.result = result;
	}

	public List<Serializable> getOtherResults() {
		return otherResults;
	}

	public void setOtherResults(List<Serializable> otherResults) {
		this.otherResults = otherResults;
	}

	public ServiceProvider getSource() {
		return source;
	}

	public void setSource(ServiceProvider source) {
		this.source = source;
	}

}
