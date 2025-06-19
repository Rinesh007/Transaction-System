package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "recharge_code")
public class RechargeCode {

    @Id
    private String code;

    private double amount;
    private boolean used;

    public RechargeCode() {}
    public RechargeCode(String code, double amount, boolean used) {
        this.code = code;
        this.amount = amount;
        this.used = used;
    }

    // Getters and Setters
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}
