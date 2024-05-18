import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

class MainTest {
    @Test
    @Timeout(22)
    @Disabled("Run it by hand, if necessary")
    @SneakyThrows
    void shouldSucceedWhenRunMainMethod() {
        Main.main(new String[] {});
    }
}