package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.ServiceCompany;
import com.example.demo.entity.ServiceProvider;

public interface ServiceCompanyRepository extends JpaRepository<ServiceCompany, String> {

	List<ServiceCompany> findByCinLikeAndProvidersIn(String cin, List<ServiceProvider> providers);

}
