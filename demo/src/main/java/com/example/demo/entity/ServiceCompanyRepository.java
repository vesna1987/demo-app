package com.example.demo.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCompanyRepository extends JpaRepository<ServiceCompany, String> {

	List<ServiceCompany> findByCinLikeAndFreeTrue(String cin);

	List<ServiceCompany> findByCinLikeAndPremiumTrue(String cin);

}
