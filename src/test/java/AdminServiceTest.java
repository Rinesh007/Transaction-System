

import org.junit.Test;
import static org.junit.Assert.*;

import main.java.AdminService;
import main.java.DBConnection;

import java.sql.Connection;

public class AdminServiceTest {

    @Test
    public void testGenerateRechargeCode() {
        Connection conn = DBConnection.getConnection();
String code = AdminService.generateRechargeCode(conn, 100.0);
        assertNotNull(code);
        assertTrue(code.length() > 0);
    }
}
