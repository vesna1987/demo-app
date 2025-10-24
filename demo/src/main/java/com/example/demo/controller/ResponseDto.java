package com.example.demo.controller;

import java.io.Serializable;
import java.util.List;

public class ResponseDto {

	private String verificationId;

	private String query;

	private Serializable result;

	private List<Serializable> otherResults;

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

}
