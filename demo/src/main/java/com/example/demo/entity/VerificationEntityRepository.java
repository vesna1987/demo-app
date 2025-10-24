package com.example.demo.entity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationEntityRepository extends JpaRepository<VerificationEntity, Long> {

	List<VerificationEntity> findByVerificationId(String verificationId);

}
