package com.example.demo.updater;

import org.springframework.stereotype.Component;

import com.example.demo.entity.ServiceCompany;
import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.dto.FreeServiceCompany;
import com.example.demo.entity.dto.PremiumServiceCompany;

@Component
public class ServiceCompanyAssembler {

	public ServiceCompany create() {
		return new ServiceCompany();
	}

	public void updateFromFreeServiceCompany(ServiceCompany entity, FreeServiceCompany dto) {
		entity.setCin(dto.getCin());
		entity.setActive(dto.isActive());
		entity.setAddress(dto.getAddress());
		entity.addProvider(ServiceProvider.FREE);
		entity.setName(dto.getName());
		entity.setRegistrationDate(dto.getRegistrationDate());
	}

	public void updateFromPremiumServiceCompany(ServiceCompany entity, PremiumServiceCompany dto) {
		entity.setFullAddress(dto.getFullAddress());
		entity.addProvider(ServiceProvider.PREMIUM);
		entity.setCin(dto.getCompanyIdentificationNumber());
		entity.setActive(dto.isActive());
		entity.setName(dto.getCompanyName());
		entity.setRegistrationDate(dto.getRegistrationDate());
	}

	public FreeServiceCompany createFreeDto(ServiceCompany entity) {
		FreeServiceCompany dto = new FreeServiceCompany();
		dto.setCin(entity.getCin());
		dto.setActive(entity.isActive());
		dto.setAddress(entity.getAddress());
		dto.setName(entity.getName());
		dto.setRegistrationDate(entity.getRegistrationDate());
		return dto;
	}

	public PremiumServiceCompany createPremiumDto(ServiceCompany entity) {
		PremiumServiceCompany dto = new PremiumServiceCompany();
		dto.setCompanyIdentificationNumber(entity.getCin());
		dto.setActive(entity.isActive());
		dto.setFullAddress(entity.getFullAddress());
		dto.setCompanyName(entity.getName());
		dto.setRegistrationDate(entity.getRegistrationDate());
		return dto;
	}

}
