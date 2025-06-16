package com.example.demo.repository;

import com.example.demo.model.RechargeCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RechargeCodeRepository extends JpaRepository<RechargeCode, Integer> {
    RechargeCode findByCode(String code);
}
