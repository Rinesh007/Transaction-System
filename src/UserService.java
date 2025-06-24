import java.sql.*;
import java.util.Scanner;

public class UserService {

    public static void redeemRechargeCode(Connection conn, String username) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recharge code: ");
        String inputCode = scanner.nextLine();

        try {
            String checkCodeSQL = "SELECT * FROM recharge_codes WHERE code = ? AND used = FALSE";
            PreparedStatement checkStmt = conn.prepareStatement(checkCodeSQL);
            checkStmt.setString(1, inputCode);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double amount = rs.getDouble("amount");

                // Update user's balance
                String updateBalanceSQL = "UPDATE users SET balance = balance + ? WHERE username = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateBalanceSQL);
                updateStmt.setDouble(1, amount);
                updateStmt.setString(2, username);
                updateStmt.executeUpdate();

                // Mark the code as used
                String markUsedSQL = "UPDATE recharge_codes SET used = TRUE WHERE code = ?";
                PreparedStatement markStmt = conn.prepareStatement(markUsedSQL);
                markStmt.setString(1, inputCode);
                markStmt.executeUpdate();

                // Log transaction
                logTransaction(conn, username, "Recharge Redeemed", amount);

                System.out.println("Recharge successful! ₹" + amount + " added.");
            } else {
                System.out.println("Invalid or already used code.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static double checkBalance(Connection conn, String username) {
    double balance = 0;
    try {
        String query = "SELECT balance FROM users WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            balance = rs.getDouble("balance");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return balance;
}


    public static void transferFunds(Connection conn, String fromUser) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter recipient username: ");
        String toUser = scanner.nextLine();
        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // flush

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return;
        }

        try {
            conn.setAutoCommit(false);

            // Check sender balance
            String balanceSQL = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement balanceStmt = conn.prepareStatement(balanceSQL);
            balanceStmt.setString(1, fromUser);
            ResultSet balanceRs = balanceStmt.executeQuery();

            if (balanceRs.next()) {
                double senderBalance = balanceRs.getDouble("balance");
                if (senderBalance < amount) {
                    System.out.println("Insufficient balance.");
                    conn.rollback();
                    return;
                }
            }

            // Deduct from sender
            String deductSQL = "UPDATE users SET balance = balance - ? WHERE username = ?";
            PreparedStatement deductStmt = conn.prepareStatement(deductSQL);
            deductStmt.setDouble(1, amount);
            deductStmt.setString(2, fromUser);
            deductStmt.executeUpdate();

            // Add to receiver
            String addSQL = "UPDATE users SET balance = balance + ? WHERE username = ?";
            PreparedStatement addStmt = conn.prepareStatement(addSQL);
            addStmt.setDouble(1, amount);
            addStmt.setString(2, toUser);
            int rowsAffected = addStmt.executeUpdate();

            if (rowsAffected == 0) {
                System.out.println("Recipient does not exist.");
                conn.rollback();
                return;
            }

            // Log transactions
            logTransaction(conn, fromUser, "Transferred to " + toUser, amount);
            logTransaction(conn, toUser, "Received from " + fromUser, amount);

            conn.commit();
            System.out.println("Funds transferred successfully!");

        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                System.out.println("Rollback failed.");
            }
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                System.out.println("Could not reset auto-commit.");
            }
        }
    }

   public static void viewTransactionHistory(Connection conn, String username, boolean isAdmin) {
    try {
        String sql;
        PreparedStatement stmt;

        if (isAdmin) {
            sql = "SELECT * FROM transactions ORDER BY timestamp DESC";
            stmt = conn.prepareStatement(sql);
        } else {
            sql = "SELECT * FROM transactions WHERE username = ? ORDER BY timestamp DESC";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
        }

        ResultSet rs = stmt.executeQuery();

        System.out.println("\n--- Transaction History ---");
        while (rs.next()) {
            String uname = rs.getString("username");
            String action = rs.getString("action");
            double amount = rs.getDouble("amount");
            Timestamp timestamp = rs.getTimestamp("timestamp");

            if (isAdmin) {
                System.out.println(timestamp + " | " + uname + " | " + action + " | ₹" + amount);
            } else {
                System.out.println(timestamp + " | " + action + " | ₹" + amount);
            }
        }

    } catch (SQLException e) {
        System.out.println("Error fetching history: " + e.getMessage());
    }
}


    // Log function
    public static void logTransaction(Connection conn, String username, String action, double amount) {
        try {
            String sql = "INSERT INTO transactions (username, action, amount) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.setDouble(3, amount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Failed to log transaction: " + e.getMessage());
        }
    }

    public static void viewTransactionHistoryByAdmin(Connection conn, String searchUsername) {
    try {
        String sql = "SELECT * FROM transactions WHERE username = ? ORDER BY timestamp DESC";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, searchUsername);
        ResultSet rs = stmt.executeQuery();

        System.out.println("\n--- Transaction History for " + searchUsername + " ---");
        boolean found = false;
        while (rs.next()) {
            found = true;
            String action = rs.getString("action");
            double amount = rs.getDouble("amount");
            Timestamp timestamp = rs.getTimestamp("timestamp");
            System.out.println(timestamp + " | " + action + " | ₹" + amount);
        }

        if (!found) {
            System.out.println("No transactions found for user: " + searchUsername);
        }

    } catch (SQLException e) {
        System.out.println("Error fetching transaction history: " + e.getMessage());
    }
}

}
