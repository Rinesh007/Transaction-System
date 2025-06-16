package com.example.demo.model;

import jakarta.persistence.*;

@Entity
public class RechargeCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String code;

    private double amount;

    private boolean used;

    // Getters and setters
}
