package com.example.demo.entity.dto;

import java.util.Date;

import com.example.demo.entity.ServiceProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class VerificationEntityDto implements DataTransferObject {

	private static final long serialVersionUID = 1L;

	@JsonIgnore
	private Long id;

	private String verificationId;
	private String queryText;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = JsonFormat.DEFAULT_TIMEZONE)
	private Date timestamp;
	private String result;
	private ServiceProvider source;

	public String getVerificationId() {
		return verificationId;
	}

	public Long getId() {
		return id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVerificationId(String verificationId) {
		this.verificationId = verificationId;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ServiceProvider getSource() {
		return source;
	}

	public void setSource(ServiceProvider source) {
		this.source = source;
	}

}
