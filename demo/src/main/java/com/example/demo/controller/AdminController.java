package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/create-user")
    public String createUser(@RequestParam String username, @RequestParam String password) {
        return adminService.createUser(username, password);
    }

    @PostMapping("/generate-code")
    public String generateRechargeCode(@RequestParam double amount) {
        return adminService.generateRechargeCode(amount);
    }

    @GetMapping("/transactions")
    public List<Transaction> getAllTransactions() {
        return adminService.getAllTransactions();
    }
}
