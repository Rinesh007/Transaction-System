import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
}
