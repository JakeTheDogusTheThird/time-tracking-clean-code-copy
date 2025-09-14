import org.example.model.Person;
import org.example.model.Presence;
import org.example.service.PersonValidator;
import org.example.service.PresenceRankCalculator;
import org.example.service.PresenceValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

public class TestPresence {
    private static final Person validPerson = new Person("John", "+37312345678");
    private static final LocalDateTime validTimeIn = LocalDateTime.of(2025, 9, 9, 9, 0);
    private static final LocalDateTime validTimeOut = LocalDateTime.of(2025, 9, 9, 18, 0);

    @Test
    public void givenNullPerson_whenIsValid_returnFalse() {
        Presence presence = new Presence(
                null,
                validTimeIn,
                validTimeOut
        );
        PersonValidator personValidator = new PersonValidator();
        PresenceValidator presenceValidator = new PresenceValidator(personValidator);
        boolean result = presenceValidator.isValid(presence);
        assertFalse(result);
    }

    @Test
    public void givenInvalidPerson_whenIsValid_returnFalse() {
        Person person = new Person(null, null);
        Presence presence = new Presence(
                person,
                validTimeIn,
                validTimeOut
        );
        PersonValidator personValidator = new PersonValidator();
        PresenceValidator presenceValidator = new PresenceValidator(personValidator);
        boolean result = presenceValidator.isValid(presence);
        assertFalse(result);
    }

    @Test
    public void givenValidPerson_whenIsValid_returnTrue() {
        Presence presence = new Presence(
                validPerson,
                validTimeIn,
                validTimeOut
        );
        PersonValidator personValidator = new PersonValidator();
        PresenceValidator presenceValidator = new PresenceValidator(personValidator);
        boolean result = presenceValidator.isValid(presence);
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTimeEntries")
    public void isValid_ShouldReturnFalseForNonValidTimeEntries(LocalDateTime timeIn, LocalDateTime timeOut) {
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        PersonValidator personValidator = new PersonValidator();
        PresenceValidator presenceValidator = new PresenceValidator(personValidator);
        boolean result = presenceValidator.isValid(presence);
        assertFalse(result);
    }

    private static Stream<Arguments> provideInvalidTimeEntries() {
        return Stream.of(
                Arguments.of(
                        null,
                        null
                ),
                Arguments.of(
                        null,
                        LocalDateTime.of(2025, 9, 9, 18, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        null
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 7, 29),
                        LocalDateTime.of(2025, 9, 9, 18, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 21, 31)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 18, 0),
                        LocalDateTime.of(2025, 9, 9, 9, 0)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidTimeEntries")
    public void isValid_ShouldReturnTrueForValidTimeEntries(LocalDateTime timeIn, LocalDateTime timeOut) {
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        PersonValidator personValidator = new PersonValidator();
        PresenceValidator presenceValidator = new PresenceValidator(personValidator);
        boolean result = presenceValidator.isValid(presence);
        assertTrue(result);
    }

    private static Stream<Arguments> provideValidTimeEntries() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 18, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 7, 31),
                        LocalDateTime.of(2025, 9, 9, 18, 0)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 21, 29)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 7, 31),
                        LocalDateTime.of(2025, 9, 9, 21, 29)
                )
        );
    }

    @Test
    public void isGood_ShouldReturnTrueWhenEightWorkedHoursAndAtTheBeginningOfDay() {
        LocalDateTime timeIn = LocalDateTime.of(2025, 9, 9, 9, 0);
        LocalDateTime timeOut = LocalDateTime.of(2025, 9, 9, 17, 30);
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        boolean result = presence.isGood();
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("provideBadTimeEntries")
    public void isGood_ShouldReturnFalseWhenWorkedHoursOutOfRangeAndNotAtTheBeginningOfDay(
            LocalDateTime timeIn,
            LocalDateTime timeOut
    ) {
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        boolean result = presence.isGood();
        assertFalse(result);
    }

    private static Stream<Arguments> provideBadTimeEntries() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 16, 30)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 18, 50)
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 40),
                        LocalDateTime.of(2025, 9, 9, 18, 30)
                ),
                Arguments.of(null, null),
                Arguments.of(validTimeIn, null),
                Arguments.of(null, validTimeOut)
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidTimeEntriesWithExpectedRanks")
    public void givenTimeEntries_whenCalculateRank_returnRank(
            LocalDateTime timeIn,
            LocalDateTime timeOut,
            double expected
    ) {
        double errorMargin = 0.001;
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        PresenceRankCalculator rankCalculator = new PresenceRankCalculator();
        double result = rankCalculator.calculateRank(presence);
        assertEquals(expected, result, errorMargin);
    }

    private static Stream<Arguments> provideValidTimeEntriesWithExpectedRanks() {
        return Stream.of(
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 0),
                        LocalDateTime.of(2025, 9, 9, 18, 0),
                        1.625
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 7, 30),
                        LocalDateTime.of(2025, 9, 9, 18, 0),
                        1.813
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 10),
                        LocalDateTime.of(2025, 9, 9, 17, 10),
                        1.167
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 30),
                        LocalDateTime.of(2025, 9, 9, 17, 30),
                        1.000
                ),
                Arguments.of(
                        LocalDateTime.of(2025, 9, 9, 9, 30),
                        LocalDateTime.of(2025, 9, 9, 14, 30),
                        0.625
                )
        );
    }

    @Test
    public void givenTimeOutBeforeTimeIn_whenCalculateRank_throwsIllegalArgumentException() {
        Presence presence = new Presence(validPerson, validTimeOut, validTimeIn);
        PresenceRankCalculator rankCalculator = new PresenceRankCalculator();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rankCalculator.calculateRank(presence));
        assertEquals("Presence must have timeIn before timeOut",  exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideNullTimeInAndTimeOut")
    public void givenNullTimeInOrTimeOut_whenCalculateRank_throwsIllegalArgumentException(
            LocalDateTime timeIn,
            LocalDateTime timeOut
    ) {
        Presence presence = new Presence(validPerson, timeIn, timeOut);
        PresenceRankCalculator rankCalculator = new PresenceRankCalculator();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> rankCalculator.calculateRank(presence));
        assertEquals("Presence must have non-null timeIn and timeOut",  exception.getMessage());
    }

    private static Stream<Arguments> provideNullTimeInAndTimeOut() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(validTimeIn, null),
                Arguments.of(null, validTimeOut)
        );
    }
}

