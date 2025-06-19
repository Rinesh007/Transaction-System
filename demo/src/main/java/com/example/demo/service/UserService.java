package com.example.demo.service;

import com.example.demo.model.RechargeCode;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.RechargeCodeRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.model.TransactionRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RechargeCodeRepository codeRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    public User login(String username, String password) {
        return userRepo.findByUsernameAndPassword(username, password);
    }

    public double checkBalance(String username) {
            User user = userRepo.findByUsername(username);
    return user != null ? user.getBalance() : 0.0;
    }

    public String redeemCode(String username, String code) {
        RechargeCode rechargeCode = codeRepo.findByCode(code);
        if (rechargeCode == null || rechargeCode.isUsed()) {
            return "Invalid or already used code.";
        }

        User user = userRepo.findById(username).orElse(null);
        if (user == null) return "User not found.";

        user.setBalance(user.getBalance() + rechargeCode.getAmount());
        rechargeCode.setUsed(true);

        userRepo.save(user);
        codeRepo.save(rechargeCode);

        Transaction tx = new Transaction(username, "Redeemed", rechargeCode.getAmount(), new Timestamp(System.currentTimeMillis()));
        transactionRepo.save(tx);

        return "Code redeemed successfully. ₹" + rechargeCode.getAmount() + " added.";
    }

    public List<Transaction> getUserTransactions(String username) {
        return transactionRepo.findByUsernameOrderByTimestampDesc(username);
    }

    // ✅ Refactored version using JPA instead of raw SQL
    @Transactional
    public String transferFunds(String sender, String recipient, double amount) {
        User senderUser = userRepo.findById(sender).orElse(null);
        User recipientUser = userRepo.findById(recipient).orElse(null);

        if (senderUser == null) return "Sender not found";
        if (recipientUser == null) return "Recipient not found";
        if (senderUser.getBalance() < amount) return "Insufficient balance";

        // Update balances
        senderUser.setBalance(senderUser.getBalance() - amount);
        recipientUser.setBalance(recipientUser.getBalance() + amount);

        userRepo.save(senderUser);
        userRepo.save(recipientUser);

        // Log transactions
        Timestamp now = new Timestamp(System.currentTimeMillis());

        Transaction senderTx = new Transaction(sender, "Transfer to " + recipient, amount, now);
        Transaction recipientTx = new Transaction(recipient, "Received from " + sender, amount, now);

        transactionRepo.save(senderTx);
        transactionRepo.save(recipientTx);

        return "Transfer successful";
    }

    // ✅ Refactored getTransactionHistory using JPA
    public List<TransactionRecord> getTransactionHistory(String username, boolean isAdmin) {
        List<Transaction> transactions = isAdmin
            ? transactionRepo.findAllByOrderByTimestampDesc()
            : transactionRepo.findByUsernameOrderByTimestampDesc(username);

        List<TransactionRecord> history = new ArrayList<>();

        for (Transaction tx : transactions) {
            history.add(new TransactionRecord(
                tx.getUsername(),
                tx.getAction(),
                tx.getAmount(),
                tx.getTimestamp()
            ));
        }

        return history;
    }
}
