package com.example.demo.listener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.demo.assembler.ServiceCompanyAssembler;
import com.example.demo.entity.ServiceCompany;
import com.example.demo.entity.dto.ServiceCompanyDto;
import com.example.demo.repository.ServiceCompanyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DemoApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ServiceCompanyAssembler serviceCompanyUpdater;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;
	@Autowired
	@Qualifier("snakeCaseObjectMapper")
	private ObjectMapper snakeCaseObjectMapper;
	@Autowired
	@Qualifier("camelCaseObjectMapper")
	private ObjectMapper camelCaseObjectMapper;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {

			InputStream inputStreamForFC = getClass().getResourceAsStream("/free_service_companies-1.json");
			List<ServiceCompanyDto> freeCompanies = snakeCaseObjectMapper.readValue(inputStreamForFC,
					new TypeReference<List<ServiceCompanyDto>>() {
					});

			Map<String, ServiceCompany> existingCompanies = new HashMap<>();
			List<ServiceCompany> serviceCompanies = new ArrayList<>();
			serviceCompanies.addAll(freeCompanies.stream().map(dto -> {
				ServiceCompany sc = new ServiceCompany();
				serviceCompanyUpdater.updateFromFreeServiceCompany(sc, dto);
				existingCompanies.put(dto.getCin(), sc);
				return sc;
			}).toList());

			InputStream inputStreamForPC = getClass().getResourceAsStream("/premium_service_companies-1.json");
			List<ServiceCompanyDto> premiumCompanies = camelCaseObjectMapper.readValue(inputStreamForPC,
					new TypeReference<List<ServiceCompanyDto>>() {
					});

			serviceCompanies.addAll(premiumCompanies.stream().map(dto -> {
				ServiceCompany sc = existingCompanies.getOrDefault(dto.getCin(), new ServiceCompany());
				serviceCompanyUpdater.updateFromPremiumServiceCompany(sc, dto);
				existingCompanies.put(dto.getCin(), sc);
				return sc;
			}).toList());

			serviceCompanyRepository.saveAll(serviceCompanies);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}