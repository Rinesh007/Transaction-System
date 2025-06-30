
import static org.junit.Assert.*;
import org.junit.Test;

import java.sql.Connection;
import main.java.DBConnection;

public class DBConnectionTest {

    @Test
    public void testConnectionNotNull() {
        Connection conn = DBConnection.getConnection();
        assertNotNull("Connection should not be null", conn);
    }
}
