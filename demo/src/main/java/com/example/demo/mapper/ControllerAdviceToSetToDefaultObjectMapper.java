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

import com.example.demo.controller.verification.VerificationsRestController;
import com.fasterxml.jackson.databind.ObjectMapper;

@ControllerAdvice(assignableTypes = VerificationsRestController.class)
public class ControllerAdviceToSetToDefaultObjectMapper implements ResponseBodyAdvice<Object> {
	@Autowired
	MappingJackson2HttpMessageConverter converter;
	@Autowired
	@Qualifier("defaultObjectMapper")
	private ObjectMapper defaultObjectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		converter.setObjectMapper(defaultObjectMapper);

		return body;
	}

}
