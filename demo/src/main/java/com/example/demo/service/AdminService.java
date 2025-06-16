package com.example.demo.service;

import com.example.demo.model.RechargeCode;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.RechargeCodeRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RechargeCodeRepository codeRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public String createUser(String username, String password) {
        if (userRepo.existsById(username)) return "User already exists.";

        User newUser = new User(username, password, "user", 0.0);
        userRepo.save(newUser);
        return "User created successfully.";
    }

    public String generateRechargeCode(double amount) {
        String code = UUID.randomUUID().toString().substring(0, 8);

        RechargeCode newCode = new RechargeCode();
        newCode.setCode(code);
        newCode.setAmount(amount);
        newCode.setUsed(false);

        codeRepo.save(newCode);
        return "Code generated: " + code;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepo.findAll();
    }
}
