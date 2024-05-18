import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.mockStatic;

class HorseTest {

    public static final String DEFAULT_HORSE_NAME = "horse";

    @Test
    void shouldReturnStringPassedToConstructorWhenCallGetName() {
        Horse horse = new Horse(DEFAULT_HORSE_NAME, 0, 0);
        assertThat(horse.getName(), is(DEFAULT_HORSE_NAME));
    }

    @Test
    void shouldReturnSpeedPassedToConstructorWhenCallGetSpeed() {
        double speed = 95;
        Horse horse = new Horse(DEFAULT_HORSE_NAME, speed, 0);
        assertThat(horse.getSpeed(), is(speed));
    }

    @Nested
    class ConstructorTest {
        @Test
        void shouldThrowAndContainErrorMessageWhenFirstParameterIsNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(null, 0, 0));
            String message = "Name cannot be null.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "  ", "    "})
        void shouldThrowAndContainErrorMessageWhenFirstParameterIsEmptyOrWhitespacesOnly(String name) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(name, 0, 0));
            String message = "Name cannot be blank.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldThrowWhenSecondParametersIsNegative() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(DEFAULT_HORSE_NAME, -1, 0));
            String message = "Speed cannot be negative.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldThrowWhenThirdParametersIsNegative() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(DEFAULT_HORSE_NAME, 0, -1));
            String message = "Distance cannot be negative.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }
    }

    @Nested
    class MoveTest {
        @Test
        void shouldInvokeGetRandomDoubleWithSpecifiedParameters() {
            try(MockedStatic<Horse> horse = mockStatic(Horse.class)) {
                new Horse(DEFAULT_HORSE_NAME, 0, 0).move();
                horse.verify(() -> Horse.getRandomDouble(0.2, 0.9));
            }
        }

        @ParameterizedTest
        @ValueSource(doubles = {10})
        void shouldAssignDistanceWithSpecificFormula(double result) {
            try(MockedStatic<Horse> horseStatic = mockStatic(Horse.class)) {
                horseStatic.when(() -> Horse.getRandomDouble(anyDouble(), anyDouble())).thenReturn(result);

                int distance = 0;
                Horse horse = new Horse(DEFAULT_HORSE_NAME, 95, distance);
                horse.move();

                assertThat(horse.getDistance(), is(distance + horse.getSpeed() * Horse.getRandomDouble(0.2, 0.9)));
            }
        }
    }
}