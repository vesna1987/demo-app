package com.example.demo.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PremiumServiceCompanyRepository extends JpaRepository<PremiumServiceCompany, String> {

	List<PremiumServiceCompany> findByCompanyIdentificationNumberLike(String cin);

}
