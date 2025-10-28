package com.example.demo.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.demo.controller.EndpointsRestController;
import com.example.demo.entity.ServiceProvider;
import com.example.demo.entity.dto.DataTransferObject;
import com.example.demo.entity.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice(assignableTypes = EndpointsRestController.class)
public class ControllerAdviceToSetObjectMapper implements ResponseBodyAdvice<DataTransferObject> {
	@Autowired
	MappingJackson2HttpMessageConverter converter;
	@Autowired
	@Qualifier("snakeCaseObjectMapper")
	private ObjectMapper snakeCaseObjectMapper;
	@Autowired
	@Qualifier("camelCaseObjectMapper")
	private ObjectMapper camelCaseObjectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public DataTransferObject beforeBodyWrite(DataTransferObject body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response) {

		if ("freeThirdParty".equals(returnType.getMethod().getName())) {
			converter.setObjectMapper(snakeCaseObjectMapper);
		} else if ("premiumThirdParty".equals(returnType.getMethod().getName())) {
			converter.setObjectMapper(camelCaseObjectMapper);
		} else if (body instanceof ResponseDto ved) {
			converter.setObjectMapper(
					ServiceProvider.FREE == ved.getSource() ? snakeCaseObjectMapper : camelCaseObjectMapper);
		} else {
			converter.setObjectMapper(camelCaseObjectMapper);
		}
		return body;
	}

}
