package main.java;
import java.util.*;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        Connection conn = DBConnection.getConnection();
        User user = AuthService.login(username, password, conn);

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome " + user.getUsername() + ", Role: " + user.getRole());

          if (user.getRole().equalsIgnoreCase("admin")) {
    while (true) {
        System.out.println("\nAdmin Menu:");
        System.out.println("1. Generate Recharge Code");
        System.out.println("2. Create New User");
        System.out.println("3. View All Transactions");
        System.out.println("4. Search Transactions by Username");
        System.out.println("5. Logout");

        int choice = scanner.nextInt();
        scanner.nextLine(); // clear buffer
        switch (choice) {
            case 1:
                AdminService.generateRechargeCode(conn,100);
                break;
            case 2:
                AdminService.createUser(conn);
                break;
            case 3:
               UserService.viewTransactionHistory(conn, "", true); // "" means show all

                break;
            case 4:
               System.out.print("Enter username to search transactions (or leave empty to show all): ");
String searchUsername = scanner.nextLine();
UserService.viewTransactionHistory(conn, searchUsername, true);

                break;
            case 5:
                System.out.println("Logging out...");
                return;
            default:
                System.out.println("Invalid option.");
        }
    }
}
 else if (user.getRole().equalsIgnoreCase("user")) {
                while (true) {
                    System.out.println("\nUser Menu:");
                    System.out.println("1. Redeem Recharge Code");
                    System.out.println("2. Check Balance");
                    System.out.println("3. Transfer Funds");
                    System.out.println("4. View My Transaction History");
                    System.out.println("5. Logout");

                    int choice = scanner.nextInt();
                    scanner.nextLine(); // clear buffer

                    switch (choice) {
                        case 1:
                            UserService.redeemRechargeCode(conn, user.getUsername());
                            break;
                        case 2:
                            UserService.checkBalance(conn, user.getUsername());
                            break;
                        case 3:
                            UserService.transferFunds(conn, user.getUsername());
                            break;
                        case 4:
                            UserService.viewTransactionHistory(conn, user.getUsername(),false);
                            break;
                        case 5:
                            System.out.println("Logging out...");
                            return;
                        default:
                            System.out.println("Invalid option.");
                    }
                }
            }

        } else {
            System.out.println("Invalid username or password.");
        }
    }
}
