package com.example.demo.controller;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.VerificationEntity;
import com.example.demo.entity.dto.ResponseDto;
import com.example.demo.entity.dto.ServiceCompanyDto;
import com.example.demo.exception.NotFoundFreeCompanyException;
import com.example.demo.repository.VerificationEntityRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class EndpointsRestController {

	@Autowired
	private VerificationEntityRepository verificationEntityRepository;

	@Autowired
	@Qualifier("snakeCaseObjectMapper")
	private ObjectMapper snakeCaseObjectMapper;
	@Autowired
	@Qualifier("camelCaseObjectMapper")
	private ObjectMapper camelCaseObjectMapper;

	@Autowired
	private CompanyRetriever companyRetriever;

	@GetMapping(value = "/free-third-party", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ServiceCompanyDto> freeThirdParty(@RequestParam(name = "query") String query) throws Exception {
		return companyRetriever.getFreeCompanies(query);

	}

	@GetMapping(value = "/premium-third-party", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ServiceCompanyDto> premiumThirdParty(@RequestParam(name = "query") String query) throws Exception {
		return companyRetriever.getPremiumCompanies(query);

	}

	@GetMapping(value = "/backend-service", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseDto backendService(@RequestParam(name = "verificationId") String verificationId,
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
		List<ServiceCompanyDto> result = premiumThirdParty(query).stream().filter(i -> i.getIsActive()).toList();
		verification.setResult(camelCaseObjectMapper.writeValueAsString(result));
		verification.setSource(ServiceProvider.PREMIUM);
		response.setSource(ServiceProvider.PREMIUM);

		List<Serializable> castedResults = result.stream().map(i -> (Serializable) i).toList();
		int sizeOfResult = result.size();
		if (sizeOfResult == 1) {
			extractSingleResult(response, castedResults);
		} else if (sizeOfResult > 1) {
			extractMultipleResults(response, castedResults);
		} else {
			noResultsFound(response, verification);
		}
	}

	private void extractMultipleResults(ResponseDto response, List<Serializable> result) {
		response.setResult(result.get(0));
		response.setOtherResults(result.subList(1, result.size()).stream().map(i -> (Serializable) i).toList());
	}

	private void extractSingleResult(ResponseDto response, List<Serializable> result) {
		response.setResult(result.get(0));
		response.setOtherResults(Collections.emptyList());
	}

	private void tryFree(String query, ResponseDto response, VerificationEntity verification)
			throws Exception, JsonProcessingException {
		List<ServiceCompanyDto> result = freeThirdParty(query).stream().filter(i -> i.getIsActive()).toList();
		verification.setResult(snakeCaseObjectMapper.writeValueAsString(result));
		verification.setSource(ServiceProvider.FREE);
		response.setSource(ServiceProvider.FREE);
		List<Serializable> castedResults = result.stream().map(i -> (Serializable) i).toList();
		int sizeOfResult = result.size();
		if (sizeOfResult == 1) {
			extractSingleResult(response, castedResults);
		} else if (sizeOfResult > 1) {
			extractMultipleResults(response, castedResults);
		} else {
			throw new NotFoundFreeCompanyException();
		}
	}

	private void noResultsFound(ResponseDto response, VerificationEntity verification) {
		verification.setResult("No matching company.");
		response.setResult("No matching company.");
	}

}
