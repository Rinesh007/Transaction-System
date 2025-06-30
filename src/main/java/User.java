package main.java;
public class User {
    private String username;
    private String role;
    private double balance; // Add balance field

    public User(String username, String role) {
        this.username = username;
        this.role = role;
        this.balance = 0.0; // Default balance
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}
