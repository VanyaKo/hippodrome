import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

class HorseTest {

    public static final String DEFAULT_NAME = "horse";
    public static final double DEFAULT_SPEED = 2.4;

    @Test
    void shouldReturnStringPassedToConstructorWhenCallGetName() {
        assertThat(new Horse(DEFAULT_NAME, DEFAULT_SPEED).getName(), is(DEFAULT_NAME));
    }

    @Test
    void shouldReturnSpeedPassedToConstructorWhenCallGetSpeed() {
        assertThat(new Horse(DEFAULT_NAME, DEFAULT_SPEED).getSpeed(), is(DEFAULT_SPEED));
    }

    @Nested
    class ConstructorTest {
        @Test
        void shouldThrowAndContainErrorMessageWhenNameParameterIsNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(null, DEFAULT_SPEED));
            String message = "Name cannot be null.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "\t", "n", " \t", " \n", "\t\n"})
        void shouldThrowAndContainErrorMessageWhenNameParameterIsEmptyOrWhitespacesOnly(String name) {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(name, DEFAULT_SPEED));
            String message = "Name cannot be blank.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldThrowWhenSpeedParameterIsNegative() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(DEFAULT_NAME, -DEFAULT_SPEED));
            String message = "Speed cannot be negative.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldThrowWhenDistanceParameterIsNegative() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Horse(DEFAULT_NAME, DEFAULT_SPEED, -1));
            String message = "Distance cannot be negative.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }
    }

    @Nested
    class MoveTest {
        @Test
        void shouldInvokeGetRandomDoubleWithSpecifiedParameters() {
            try(MockedStatic<Horse> horse = mockStatic(Horse.class)) {
                new Horse(DEFAULT_NAME, DEFAULT_SPEED).move();
                horse.verify(() -> Horse.getRandomDouble(0.2, 0.9));
            }
        }

        @ParameterizedTest
        @CsvSource({"4,10"})
        void shouldAssignDistanceWithSpecificFormula(double distance, double result) {
            try(MockedStatic<Horse> horseStatic = mockStatic(Horse.class)) {
                horseStatic.when(() -> Horse.getRandomDouble(0.2, 0.9)).thenReturn(result);
                Horse horse = new Horse(DEFAULT_NAME, DEFAULT_SPEED, distance);
                double expectedDistance = horse.getDistance() + horse.getSpeed() * Horse.getRandomDouble(0.2, 0.9);

                horse.move();

                assertThat(horse.getDistance(), is(expectedDistance));
            }
        }
    }
}