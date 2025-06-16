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

    public String getUsername() {
        return username;
    }

    public String getAction() {
        return action;
    }

    public double getAmount() {
        return amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
