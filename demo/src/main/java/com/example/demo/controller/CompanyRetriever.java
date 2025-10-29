package com.example.demo.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.assembler.ServiceCompanyAssembler;
import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.dto.ServiceCompanyDto;
import com.example.demo.repository.ServiceCompanyRepository;

@Component
public class CompanyRetriever {

	@Autowired
	private ServiceCompanyRepository serviceCompanyRepository;

	@Autowired
	private ServiceCompanyAssembler serviceCompanyAssembler;

	public List<ServiceCompanyDto> getFreeCompanies(String query) {
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		if (randomNumber <= 40) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "503 Service Unavailable");
		}

		return serviceCompanyRepository.findByCinLikeAndProvidersIn("%" + query + "%", List.of(ServiceProvider.FREE))
				.stream().map(sc -> serviceCompanyAssembler.createFreeDto(sc)).toList();
	}

	public List<ServiceCompanyDto> getPremiumCompanies(String query) {
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		if (randomNumber <= 10) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "503 Service Unavailable");
		}

		return serviceCompanyRepository.findByCinLikeAndProvidersIn("%" + query + "%", List.of(ServiceProvider.PREMIUM))
				.stream().map(sc -> serviceCompanyAssembler.createPremiumDto(sc)).toList();
	}

}
