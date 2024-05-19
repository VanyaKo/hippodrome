import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class HippodromeTest {
    @Nested
    class ConstructorTest {
        @Test
        void shouldThrowAndContainErrorMessageWhenInputIsNull() {
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Hippodrome(null));
            String message = "Horses cannot be null.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldThrowAndContainErrorMessageWhenInputIsEmptyList() {
            List<Horse> horses = new ArrayList<>();
            IllegalArgumentException illegalArgumentException = assertThrows(IllegalArgumentException.class,
                    () -> new Hippodrome(horses));
            String message = "Horses cannot be empty.";
            assertThat(illegalArgumentException.getMessage(), containsString(message));
        }

        @Test
        void shouldReturnListPassedToConstructorWhenCallGetHorses() {
            List<Horse> horses = new ArrayList<>();
            for(int i = 0; i < 30; i++) {
                horses.add(new Horse("Horse #" + i, ThreadLocalRandom.current().nextDouble(2.4, 3)));
            }
            Hippodrome hippodrome = new Hippodrome(horses);
            assertEquals(horses, hippodrome.getHorses());
        }

        @Test
        void shouldTriggerMoveMethodForEachHorseWhenCallMove() {
            List<Horse> horses = new ArrayList<>();
            int count = 50;
            for(int i = 0; i < count; i++) {
                horses.add(mock(Horse.class));
            }

            Hippodrome hippodrome = new Hippodrome(horses);
            hippodrome.move();

            for(int i = 0; i < count; i++) {
                verify(horses.get(i)).move();
            }
        }

        @Test
        void shouldReturnHorseWithMaxDistanceWhenCallGetWinner() {
            List<Horse> horses = List.of(new Horse("Sugar", 2.5, 10), new Horse("Spirit", 2.4, 20));

            Hippodrome hippodrome = new Hippodrome(horses);
            double maxDistance = hippodrome.getWinner().getDistance();

            List<Double> distances = horses.stream()
                    .map(Horse::getDistance)
                    .collect(Collectors.toList());
            assertThat(distances, everyItem(lessThanOrEqualTo(maxDistance)));
        }
    }

}