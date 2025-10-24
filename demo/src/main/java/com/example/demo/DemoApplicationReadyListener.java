package com.example.demo;

import java.io.InputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.example.demo.entity.FreeServiceCompany;
import com.example.demo.entity.FreeServiceCompanyRepository;
import com.example.demo.entity.PremiumServiceCompany;
import com.example.demo.entity.PremiumServiceCompanyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class DemoApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

	@Autowired
	private FreeServiceCompanyRepository freeServiceCompanyRepository;
	@Autowired
	private PremiumServiceCompanyRepository premiumServiceCompanyRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		try {
			System.out.println(
					"Executing logic after the application is fully ready and serving requests (ApplicationReadyEvent)");

			InputStream inputStreamForFC = getClass().getResourceAsStream("/free_service_companies-1.json");
			List<FreeServiceCompany> freeCompanies = objectMapper.readValue(inputStreamForFC,
					new TypeReference<List<FreeServiceCompany>>() {
					});

			freeServiceCompanyRepository.saveAll(freeCompanies);

			InputStream inputStreamForPC = getClass().getResourceAsStream("/premium_service_companies-1.json");
			List<PremiumServiceCompany> premiumCompanies = objectMapper.readValue(inputStreamForPC,
					new TypeReference<List<PremiumServiceCompany>>() {
					});

			premiumServiceCompanyRepository.saveAll(premiumCompanies);
			System.out.println("Data loaded from JSON file.");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}