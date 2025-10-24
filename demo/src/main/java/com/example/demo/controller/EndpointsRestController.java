package com.example.demo.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.FreeServiceCompany;
import com.example.demo.entity.FreeServiceCompanyRepository;
import com.example.demo.entity.PremiumServiceCompany;
import com.example.demo.entity.PremiumServiceCompanyRepository;
import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.VerificationEntity;
import com.example.demo.entity.VerificationEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EndpointsRestController {

	@Autowired
	private FreeServiceCompanyRepository freeServiceCompanyRepository;
	@Autowired
	private PremiumServiceCompanyRepository premiumServiceCompanyRepository;
	@Autowired
	private VerificationEntityRepository verificationEntityRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("/hello")
	public String sayHello() {
		freeServiceCompanyRepository.findAll();

		return "Hello from Spring Boot REST Controller!";
	}

	@GetMapping(value = "/free-third-party", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<FreeServiceCompany> freeThirdParty(@RequestParam(name = "query") String query) throws Exception {
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		if (randomNumber <= 40) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "503 Service Unavailable");
		}

		return freeServiceCompanyRepository.findByCinLike("%" + query + "%");

		// return
		// objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

	}

	@GetMapping(value = "/premium-third-party", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PremiumServiceCompany> premiumThirdParty(@RequestParam(name = "query") String query) throws Exception {
		Random random = new Random();
		int randomNumber = random.nextInt(100) + 1;
		if (randomNumber <= 10) {
			throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "503 Service Unavailable");
		}

		List<PremiumServiceCompany> result = premiumServiceCompanyRepository
				.findByCompanyIdentificationNumberLike("%" + query + "%");
		return result;

		// return
		// objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);

	}

	@GetMapping(value = "/backend-service", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDto premiumThirdParty(@RequestParam(name = "verificationId") String verificationId,
			@RequestParam(name = "query") String query) throws Exception {
		ResponseDto response = new ResponseDto();
		response.setVerificationId(verificationId);
		response.setQuery(query);
		VerificationEntity verification = new VerificationEntity();
		verification.setVerificationId(verificationId);
		verification.setQueryText(query);
		try {
			tryFree(query, response, verification);
		} catch (ResponseStatusException | NotFoundFreeCompanyException ex) {
			if ((ex instanceof ResponseStatusException rse
					&& HttpStatus.SERVICE_UNAVAILABLE.equals(rse.getStatusCode()))
					|| ex instanceof NotFoundFreeCompanyException) {
				try {
					tryPremium(query, response, verification);
				} catch (ResponseStatusException rseAgain) {
					if (HttpStatus.SERVICE_UNAVAILABLE.equals(rseAgain.getStatusCode())) {
						String errorMessage = "The third parties are down.";
						verification.setResult(errorMessage);
						response.setResult(errorMessage);
					}
				}
			}

		}
		verification.setTimestamp(new Date(System.currentTimeMillis()));
		verificationEntityRepository.save(verification);
		return response;

	}

	private void tryPremium(String query, ResponseDto response, VerificationEntity verification)
			throws Exception, JsonProcessingException {
		List<PremiumServiceCompany> result = premiumThirdParty(query).stream().filter(i -> i.isActive()).toList();
		verification.setResult(objectMapper.writeValueAsString(result));
		verification.setSource(ServiceProvider.PREMIUM_THIRD_PARTY);
		if (result.size() == 1) {
			response.setResult(result.get(0));
			response.setOtherResults(Collections.emptyList());
		} else if (result.size() > 1) {
			response.setResult(result.get(0));
			response.setOtherResults(result.subList(1, result.size()).stream().map(i -> (Serializable) i).toList());
		} else {
			noResultsFound(response, verification);
		}
	}

	private void tryFree(String query, ResponseDto response, VerificationEntity verification)
			throws Exception, JsonProcessingException {
		List<FreeServiceCompany> result = freeThirdParty(query).stream().filter(i -> i.isActive()).toList();
		verification.setResult(objectMapper.writeValueAsString(result));
		verification.setSource(ServiceProvider.FREE_THIRD_PARTY);
		if (result.size() == 1) {
			response.setResult(result.get(0));
			response.setOtherResults(Collections.emptyList());
		} else if (result.size() > 1) {
			response.setResult(result.get(0));
			response.setOtherResults(result.subList(1, result.size()).stream().map(i -> (Serializable) i).toList());
		} else {
			throw new NotFoundFreeCompanyException();
		}
	}

	@GetMapping(value = "/retrieving-verifications", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<VerificationEntity> retrievingVerifications(
			@RequestParam(name = "verificationId") String verificationId) throws Exception {
		return verificationEntityRepository.findByVerificationId(verificationId);

	}

	private void noResultsFound(ResponseDto response, VerificationEntity verification) {
		verification.setResult("No matching company.");
		response.setResult("No matching company.");
	}

}
