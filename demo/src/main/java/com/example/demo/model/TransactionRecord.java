package com.example.demo.model; 

import java.sql.Timestamp;

public class TransactionRecord {

    private String username;
    private String action;
    private double amount;
    private Timestamp timestamp;

    public TransactionRecord(String username, String action, double amount, Timestamp timestamp) {
        this.username = username;
        this.action = action;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Timestamp getTimestamp() { return timestamp; }
    public void setTimestamp(Timestamp timestamp) { this.timestamp = timestamp; }
}
