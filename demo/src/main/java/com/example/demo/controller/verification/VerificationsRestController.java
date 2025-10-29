package com.example.demo.controller.verification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.assembler.VerificationAssembler;
import com.example.demo.entity.dto.VerificationEntityDto;
import com.example.demo.repository.VerificationEntityRepository;

@RestController
public class VerificationsRestController {
	@Autowired
	private VerificationEntityRepository verificationEntityRepository;

	@Autowired
	private VerificationAssembler verificationAssembler;

	@GetMapping(value = "/retrieving-verifications", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VerificationEntityDto> retrievingVerifications(
			@RequestParam(name = "verificationId") String verificationId) throws Exception {
		return verificationAssembler
				.createDtos(verificationEntityRepository.findByVerificationIdOrderByIdAsc(verificationId));

	}

}
