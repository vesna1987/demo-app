package com.example.demo.updater;

import org.springframework.stereotype.Component;

import com.example.demo.entity.ServiceCompany;
import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.dto.ServiceCompanyDto;

@Component
public class ServiceCompanyAssembler {

	public ServiceCompany create() {
		return new ServiceCompany();
	}

	public void updateFromFreeServiceCompany(ServiceCompany entity, ServiceCompanyDto dto) {
		commonUpdate(entity, dto);
		entity.addProvider(ServiceProvider.FREE);
		entity.setAddress(dto.getAddress());
	}

	public void updateFromPremiumServiceCompany(ServiceCompany entity, ServiceCompanyDto dto) {
		commonUpdate(entity, dto);
		entity.addProvider(ServiceProvider.PREMIUM);
		entity.setFullAddress(dto.getFullAddress());
	}

	private void commonUpdate(ServiceCompany entity, ServiceCompanyDto dto) {
		entity.setCin(dto.getCin());
		entity.setActive(dto.getIsActive());

		entity.setName(dto.getName());
		entity.setRegistrationDate(dto.getRegistrationDate());
	}

	public ServiceCompanyDto createFreeDto(ServiceCompany entity) {
		ServiceCompanyDto dto = new ServiceCompanyDto();
		commonAssemble(entity, dto);
		dto.setAddress(entity.getAddress());
		return dto;
	}

	private void commonAssemble(ServiceCompany entity, ServiceCompanyDto dto) {
		dto.setCin(entity.getCin());
		dto.setIsActive(entity.isActive());
		dto.setName(entity.getName());
		dto.setRegistrationDate(entity.getRegistrationDate());
	}

	public ServiceCompanyDto createPremiumDto(ServiceCompany entity) {
		ServiceCompanyDto dto = new ServiceCompanyDto();
		commonAssemble(entity, dto);
		dto.setFullAddress(entity.getFullAddress());
		return dto;
	}

}
