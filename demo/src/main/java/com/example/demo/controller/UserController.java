package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.service.UserService;

import main.java.com.example.demo.dto.TransferRequest;
import main.java.com.example.demo.model.TransactionRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/balance")
    public double checkBalance(@RequestParam String username) {
        return userService.checkBalance(username);
    }

    @PostMapping("/redeem")
    public String redeemCode(@RequestParam String username, @RequestParam String code) {
        return userService.redeemCode(username, code);
    }

    @GetMapping("/transactions")
    public List<Transaction> getUserTransactions(@RequestParam String username) {
        return userService.getUserTransactions(username);
    }
    @PostMapping("/transfer")
public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request) {
    String result = userService.transferFunds(request.getSender(), request.getRecipient(), request.getAmount());
    if (result.equals("Transfer successful")) {
        return ResponseEntity.ok(result);
    } else {
        return ResponseEntity.badRequest().body(result);
    }
}
@GetMapping("/transactions")
public ResponseEntity<List<TransactionRecord>> getTransactionHistory(
        @RequestParam String username,
        @RequestParam boolean isAdmin
) {
    List<TransactionRecord> history = userService.getTransactionHistory(username, isAdmin);
    return ResponseEntity.ok(history);
}


}
