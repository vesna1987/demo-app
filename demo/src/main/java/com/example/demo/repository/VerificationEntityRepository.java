package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.VerificationEntity;

public interface VerificationEntityRepository extends JpaRepository<VerificationEntity, Long> {

	List<VerificationEntity> findByVerificationIdOrderByIdAsc(String verificationId);

}
