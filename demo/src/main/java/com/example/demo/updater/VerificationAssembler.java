package com.example.demo.updater;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.demo.entity.VerificationEntity;
import com.example.demo.entity.dto.VerificationEntityDto;

@Component
public class VerificationAssembler {

	public VerificationEntityDto createDto(VerificationEntity entity) {
		VerificationEntityDto dto = new VerificationEntityDto();
		dto.setId(entity.getId());
		dto.setQueryText(entity.getQueryText());
		dto.setResult(entity.getResult());
		dto.setSource(entity.getSource());
		dto.setTimestamp(entity.getTimestamp());
		dto.setVerificationId(entity.getVerificationId());
		return dto;
	}

	public List<VerificationEntityDto> createDtos(List<VerificationEntity> entities) {
		return entities.stream().map(this::createDto).toList();
	}

}
