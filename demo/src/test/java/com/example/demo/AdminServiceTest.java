package com.example.demo;

import com.example.demo.model.RechargeCode;
import com.example.demo.repository.RechargeCodeRepository;
import com.example.demo.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceTest {

    @Mock
    private RechargeCodeRepository rechargeCodeRepository;

    @InjectMocks
    private AdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateRechargeCode() {
        RechargeCode code = new RechargeCode("ABCD1234", 100.0,false);
        when(rechargeCodeRepository.save(any())).thenReturn(code);

        String generated = adminService.generateRechargeCode(100.0);
        assertNotNull(generated);
        assertTrue(generated.length() > 0);
    }
}
