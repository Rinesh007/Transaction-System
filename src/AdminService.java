import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Scanner;

public class AdminService {

    public static void generateRechargeCode(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter amount for recharge code: ");
        double amount = sc.nextDouble();

        String code = CodeGenerator.generateCode();

        String sql = "INSERT INTO recharge_codes (code, amount) VALUES (?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, code);
            stmt.setDouble(2, amount);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Recharge code generated: " + code + " (₹" + amount + ")");
            } else {
                System.out.println("Failed to generate recharge code.");
            }

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void createUser(Connection conn) 
    {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter new username: ");
    String newUsername = scanner.nextLine();
    System.out.print("Enter new password: ");
    String newPassword = scanner.nextLine();

    String role = "user"; 

    try {
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newUsername);
        stmt.setString(2, newPassword);
        stmt.setString(3, role);
        stmt.executeUpdate();

        System.out.println("Normal user created successfully.");
    } catch (SQLIntegrityConstraintViolationException e) {
        System.out.println("Username already exists. Choose another.");
    } catch (SQLException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

}
