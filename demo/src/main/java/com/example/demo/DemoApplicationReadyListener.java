package com.example.demo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.FreeServiceCompany;
import com.example.demo.entity.PremiumServiceCompany;
import com.example.demo.entity.ServiceCompany;
import com.example.demo.entity.ServiceCompanyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DemoApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private ServiceCompanyUpdater serviceCompanyUpdater;
	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			System.out.println(
					"Executing logic after the application is fully ready and serving requests (ApplicationReadyEvent)");

			InputStream inputStreamForFC = getClass().getResourceAsStream("/free_service_companies-1.json");
			List<FreeServiceCompany> freeCompanies = objectMapper.readValue(inputStreamForFC,
					new TypeReference<List<FreeServiceCompany>>() {
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
			List<PremiumServiceCompany> premiumCompanies = objectMapper.readValue(inputStreamForPC,
					new TypeReference<List<PremiumServiceCompany>>() {
					});

			serviceCompanies.addAll(premiumCompanies.stream().map(dto -> {
				ServiceCompany sc = existingCompanies.getOrDefault(dto.getCompanyIdentificationNumber(),
						new ServiceCompany());
				serviceCompanyUpdater.updateFromPremiumServiceCompany(sc, dto);
				existingCompanies.put(dto.getCompanyIdentificationNumber(), sc);
				return sc;
			}).toList());

			serviceCompanyRepository.saveAll(serviceCompanies);
			System.out.println("Data loaded from JSON file.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}