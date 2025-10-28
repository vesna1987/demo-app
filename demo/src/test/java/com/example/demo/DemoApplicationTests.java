package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.example.demo.entity.dto.ResponseDto;
import com.example.demo.entity.dto.VerificationEntityDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	private String getBaseUrl() {
		return "http://localhost:8080";
	}

	@Test
	void testBackendService() {
		String verificationId = "AAAA";
		ResponseEntity<List<VerificationEntityDto>> verifications = restTemplate.exchange(
				getBaseUrl() + "/retrieving-verifications?verificationId=" + verificationId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<VerificationEntityDto>>() {
				});
		int numberOfVerification = verifications.getBody().size();

		ResponseEntity<ResponseDto> result = restTemplate.getForEntity(
				getBaseUrl() + "/backend-service?verificationId=" + verificationId + "&query=W", ResponseDto.class);

		assertThat(result.getStatusCode().is2xxSuccessful()).isTrue();

		verifications = restTemplate.exchange(
				getBaseUrl() + "/retrieving-verifications?verificationId=" + verificationId, HttpMethod.GET, null,
				new ParameterizedTypeReference<List<VerificationEntityDto>>() {
				});

		assertThat(verifications.getBody()).hasSize(numberOfVerification + 1);
		assertThat(verifications.getBody().getLast().getVerificationId()).isEqualTo("AAAA");
	}

}
