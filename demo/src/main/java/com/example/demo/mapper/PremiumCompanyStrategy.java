package com.example.demo.mapper;

import java.util.Map;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.LowerCamelCaseStrategy;

public class PremiumCompanyStrategy extends LowerCamelCaseStrategy {

	private static final long serialVersionUID = 1L;

	Map<String, String> fieldNameMapper = Map.ofEntries(Map.entry("cin", "companyIdentificationNumber"),
			Map.entry("name", "companyName"));

	@Override
	public String translate(String input) {
		return super.translate(fieldNameMapper.getOrDefault(input, input));
	}

}
