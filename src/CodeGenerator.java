package src;
import java.util.UUID;

public class CodeGenerator {
    public static String generateCode() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();  // e.g., "A8F3C9D2"
    }
}
