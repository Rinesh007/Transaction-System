package com.example.demo.dto;

public class TransferRequest {
    private String sender;
    private String recipient;
    private double amount;

    // Default constructor for JSON deserialization
    public TransferRequest() {}

    public TransferRequest(String sender, String recipient, double amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    // Getters and setters
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
