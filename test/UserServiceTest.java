package test;

import com.example.transaction.UserService;
import com.example.transaction.DBConnection;

import org.junit.Test;
import java.sql.Connection;

import static org.junit.Assert.*;

public class UserServiceTest {

    @Test
    public void testCheckBalance() {
        Connection conn = DBConnection.getConnection();
        String username = "rinesh";

        double balance = UserService.checkBalance(conn, username);

        assertTrue(balance >= 0);
    }
}
