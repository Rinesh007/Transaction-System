
import org.junit.Test;
import static org.junit.Assert.*;

import main.java.User;

public class UserTest {

    @Test
    public void testUserConstructorAndBalance() {
        User user = new User("rinesh", "pass123");

        user.setBalance(150.0);
        assertEquals(150.0, user.getBalance(), 0.01);
    }

    @Test
    public void testUsername() {
        User user = new User("rinesh", "pass123");
        assertEquals("rinesh", user.getUsername());
    }
}
