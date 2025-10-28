package com.example.demo;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import com.example.demo.mapper.PremiumCompanyStrategy;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getDefault());
		return objectMapper;
	}

	@Bean("snakeCaseObjectMapper")
	public ObjectMapper snakeCaseObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getDefault());
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept("full_address"));
		objectMapper.setFilterProvider(filterProvider);
		return objectMapper;
	}

	@Bean("camelCaseObjectMapper")
	public ObjectMapper camelCaseObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.setTimeZone(TimeZone.getDefault());
		objectMapper.setPropertyNamingStrategy(new PremiumCompanyStrategy());
		SimpleFilterProvider filterProvider = new SimpleFilterProvider();
		filterProvider.addFilter("myFilter", SimpleBeanPropertyFilter.serializeAllExcept("address"));
		objectMapper.setFilterProvider(filterProvider);
		return objectMapper;
	}

	@Bean
	public MappingJackson2HttpMessageConverter converter() {
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		converter.setObjectMapper(builder.build());
		return converter;
	}
}
