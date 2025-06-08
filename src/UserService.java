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

                System.out.println("Recharge successful! Amount added: ₹" + amount);
            } else {
                System.out.println("Invalid or already used code.");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void checkBalance(Connection conn, String username) {
        try {
            String sql = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                System.out.println("Your current balance is: ₹" + balance);
            } else {
                System.out.println("User not found.");
            }

        } catch (Exception e) {
            System.out.println("Error checking balance: " + e.getMessage());
        }
    }

    public static void transferFunds(Connection conn, String senderUsername) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter recipient's username: ");
        String receiverUsername = scanner.nextLine();

        System.out.print("Enter amount to transfer: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();

        try {
            String checkBalanceSQL = "SELECT balance FROM users WHERE username = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkBalanceSQL);
            checkStmt.setString(1, senderUsername);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");

                if (balance >= amount) {
                    conn.setAutoCommit(false); // begin transaction

                    String deductSQL = "UPDATE users SET balance = balance - ? WHERE username = ?";
                    PreparedStatement deductStmt = conn.prepareStatement(deductSQL);
                    deductStmt.setDouble(1, amount);
                    deductStmt.setString(2, senderUsername);
                    deductStmt.executeUpdate();

                    String addSQL = "UPDATE users SET balance = balance + ? WHERE username = ?";
                    PreparedStatement addStmt = conn.prepareStatement(addSQL);
                    addStmt.setDouble(1, amount);
                    addStmt.setString(2, receiverUsername);
                    int rows = addStmt.executeUpdate();

                    if (rows == 0) {
                        conn.rollback();
                        System.out.println("Recipient username does not exist. Transaction cancelled.");
                    } else {
                        conn.commit();
                        System.out.println("₹" + amount + " transferred to " + receiverUsername);
                    }

                    conn.setAutoCommit(true);
                } else {
                    System.out.println("Insufficient balance.");
                }

            } else {
                System.out.println("Sender not found.");
            }

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Transfer failed: " + e.getMessage());
        }
    }
}
