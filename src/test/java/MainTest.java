import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MainTest {
    @Test
    @Timeout(22)
    @Disabled
    void shouldSucceedWhenRunMainMethod() {
        try {
            Main.main(new String[] {});
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}