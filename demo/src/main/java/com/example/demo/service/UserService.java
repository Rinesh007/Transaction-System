package com.example.demo.service;

import com.example.demo.model.RechargeCode;
import com.example.demo.model.Transaction;
import com.example.demo.model.User;
import com.example.demo.repository.RechargeCodeRepository;
import com.example.demo.repository.TransactionRepository;
import com.example.demo.repository.UserRepository;

import main.java.com.example.demo.model.TransactionRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return userRepo.findById(username).map(User::getBalance).orElse(0.0);
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
    public String transferFunds(String sender, String recipient, double amount) {
    String checkBalanceSQL = "SELECT balance FROM users WHERE username = ?";
    String updateSenderSQL = "UPDATE users SET balance = balance - ? WHERE username = ?";
    String updateRecipientSQL = "UPDATE users SET balance = balance + ? WHERE username = ?";
    String logSQL = "INSERT INTO transactions (username, action, amount) VALUES (?, ?, ?)";

    try (Connection conn = DBConnection.getConnection()) {
        conn.setAutoCommit(false);

        // Check sender balance
        PreparedStatement checkStmt = conn.prepareStatement(checkBalanceSQL);
        checkStmt.setString(1, sender);
        ResultSet rs = checkStmt.executeQuery();

        if (!rs.next()) return "Sender not found";
        double balance = rs.getDouble("balance");
        if (balance < amount) return "Insufficient balance";

        // Deduct from sender
        PreparedStatement deductStmt = conn.prepareStatement(updateSenderSQL);
        deductStmt.setDouble(1, amount);
        deductStmt.setString(2, sender);
        deductStmt.executeUpdate();

        // Add to recipient
        PreparedStatement addStmt = conn.prepareStatement(updateRecipientSQL);
        addStmt.setDouble(1, amount);
        addStmt.setString(2, recipient);
        int rowsAffected = addStmt.executeUpdate();

        if (rowsAffected == 0) {
            conn.rollback();
            return "Recipient not found";
        }

        // Log transactions
        PreparedStatement logStmt = conn.prepareStatement(logSQL);
        logStmt.setString(1, sender);
        logStmt.setString(2, "Transfer to " + recipient);
        logStmt.setDouble(3, amount);
        logStmt.executeUpdate();

        logStmt.setString(1, recipient);
        logStmt.setString(2, "Received from " + sender);
        logStmt.setDouble(3, amount);
        logStmt.executeUpdate();

        conn.commit();
        return "Transfer successful";

    } catch (SQLException e) {
        e.printStackTrace();
        return "Error occurred during transfer";
    }
}
public List<TransactionRecord> getTransactionHistory(String username, boolean isAdmin) {
    List<TransactionRecord> history = new ArrayList<>();
    String sql = isAdmin
        ? "SELECT * FROM transactions ORDER BY timestamp DESC"
        : "SELECT * FROM transactions WHERE username = ? ORDER BY timestamp DESC";

    try (Connection conn = DBConnection.getConnection()) {
        PreparedStatement stmt = conn.prepareStatement(sql);

        if (!isAdmin) {
            stmt.setString(1, username);
        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            TransactionRecord record = new TransactionRecord(
                rs.getString("username"),
                rs.getString("action"),
                rs.getDouble("amount"),
                rs.getTimestamp("timestamp")
            );
            history.add(record);
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return history;
}



}
