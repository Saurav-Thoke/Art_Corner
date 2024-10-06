package com.saurav.ArtCorner.repository;

import com.saurav.ArtCorner.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode,Long> {

    VerificationCode findByEmail(String email);
}