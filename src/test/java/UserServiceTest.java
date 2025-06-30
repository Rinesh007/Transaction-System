
import org.junit.Test;
import static org.junit.Assert.*;

import java.sql.Connection;

import main.java.DBConnection;
import main.java.UserService;

public class UserServiceTest {

    @Test
    public void testCheckBalance() {
        Connection conn = DBConnection.getConnection();
        String username = "rinesh";

        double balance = UserService.checkBalance(conn, username);

        assertTrue(balance >= 0);
    }
}
