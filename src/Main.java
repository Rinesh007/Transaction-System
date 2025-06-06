import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter username:");
        String username = scanner.nextLine();

        System.out.println("Enter password:");
        String password = scanner.nextLine();

        User user = AuthService.login(username, password);

        if (user != null) {
            System.out.println("Login successful!");
            System.out.println("Welcome " + user.getUsername() + ", Role: " + user.getRole());
        } else {
            System.out.println("Invalid username or password.");
        }
    }
}
