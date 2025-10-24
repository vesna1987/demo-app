package com.example.demo.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeServiceCompanyRepository extends JpaRepository<FreeServiceCompany, String> {

	List<FreeServiceCompany> findByCinLike(String cin);

}
