package com.example.demo.controller;

import com.example.demo.model.Transaction;
import com.example.demo.model.TransactionRecord;
import com.example.demo.dto.TransferRequest;
import com.example.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Check user's current balance
    @GetMapping("/balance")
    public double checkBalance(@RequestParam String username) {
        return userService.checkBalance(username);
    }

    // Redeem a recharge code
    @PostMapping("/redeem")
    public String redeemCode(@RequestParam String username, @RequestParam String code) {
        return userService.redeemCode(username, code);
    }

    // Get simple transaction list (for normal users)
    @GetMapping("/transactions")
    public List<Transaction> getUserTransactions(@RequestParam String username) {
        return userService.getUserTransactions(username);
    }

    // Transfer funds between users
    @PostMapping("/transfer")
    public ResponseEntity<String> transferFunds(@RequestBody TransferRequest request) {
        String result = userService.transferFunds(request.getSender(), request.getRecipient(), request.getAmount());
        if (result.equals("Transfer successful")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // Get full transaction history (for admin users)
    @GetMapping("/transactions/history")
    public ResponseEntity<List<TransactionRecord>> getTransactionHistory(
            @RequestParam String username,
            @RequestParam boolean isAdmin
    ) {
        List<TransactionRecord> history = userService.getTransactionHistory(username, isAdmin);
        return ResponseEntity.ok(history);
    }
}
