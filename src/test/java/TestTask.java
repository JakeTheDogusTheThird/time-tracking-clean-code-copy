import org.example.model.Task;
import org.example.service.CedacriTaskValidator;
import org.example.service.TaskValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestTask {
    private static final String validProjectName = "Valid Project Name";
    private static final String validTaskName = "Valid Task Name";
    private static final LocalDateTime validStart = LocalDateTime.of(2025, 9, 9, 9, 0);

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "\u000B", "\f", "\r"})
    public void givenBlankProjectName_whenIsValid_returnFalse(String projectName) {
        Task task = new Task(projectName, validTaskName, validStart, validStart.plusHours(8), true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertFalse(result);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "\u000B", "\f", "\r"})
    public void givenBlankTaskName_whenIsValid_returnFalse(String taskName) {
        Task task = new Task(validProjectName, taskName, validStart, validStart.plusHours(8), true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertFalse(result);
    }

    @Test
    public void givenValidProjectName_whenIsValid_returnTrue() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertTrue(result);
    }

    @Test
    public void givenValidTaskName_whenIsValid_returnTrue() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTimeEntries")
    public void isValid_ShouldReturnFalseForNonValidTimeEntries(LocalDateTime start, LocalDateTime end) {
        Task task = new Task(validProjectName, validTaskName, start, end, true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertFalse(result);
    }

    public static Stream<Arguments> provideInvalidTimeEntries() {
        return Stream.of(
                Arguments.of(null, validStart.plusHours(8)),
                Arguments.of(validStart, null),
                Arguments.of(null, null),
                Arguments.of(validStart.plusHours(8), validStart)
        );
    }

    @Test
    public void givenValidTimeEntries_whenIsValid_returnTrue() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        TaskValidator taskValidator = new CedacriTaskValidator();
        boolean result = taskValidator.isValid(task);
        assertTrue(result);
    }

    @Test
    public void givenNotCompletedTask_whenIsGood_returnFalse() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), false);
        boolean result = task.isGood();
        assertFalse(result);
    }

    @Test
    public void givenOverEightHoursWorkedTime_whenIsGood_returnFalse() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(9), true);
        boolean result = task.isGood();
        assertFalse(result);
    }

    @Test
    public void givenEightHoursWorkedTime_whenIsGood_returnTrue() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        boolean result = task.isGood();
        assertTrue(result);
    }

    @ParameterizedTest
    @MethodSource("provideNullStartOrNullEnd")
    public void givenNullStartOrNullEnd_whenIsGood_returnFalse(
            LocalDateTime start,
            LocalDateTime end
    ) {
        Task task = new Task(validProjectName, validTaskName, start, end, true);
        boolean result = task.isGood();
        assertFalse(result);
    }

    @Test
    public void givenEndBeforeStart_whenIsGood_returnFalse() {
        Task task = new Task(validProjectName, validTaskName, validStart.plusHours(1), validStart, true);
        boolean result = task.isGood();
        assertFalse(result);
    }

    private static Stream<Arguments> provideNullStartOrNullEnd() {
        return Stream.of(
                Arguments.of(null, null),
                Arguments.of(validStart, null),
                Arguments.of(null, validStart)
        );
    }

    @ParameterizedTest
    @MethodSource("provideRatingCases")
    public void givenRatingCases_whenGetRating_returnRating(
            LocalDateTime timeIn,
            LocalDateTime timeOut,
            boolean completed,
            double expected
    ) {
        double errorMargin = 0.001;
        Task task = new Task(validProjectName, validTaskName, timeIn, timeOut, completed);
        double result = task.getRating();
        assertEquals(expected, result, errorMargin);
    }

    private static Stream<Arguments> provideRatingCases() {
        // start, end, completed, expected result
        return Stream.of(
                Arguments.of(validStart, validStart.plusHours(8), true, 1.0),
                Arguments.of(validStart, validStart.plusHours(8), false, -1.0),
                Arguments.of(validStart, validStart.plusHours(4), true, 1.5),
                Arguments.of(validStart, validStart.plusHours(4), false, -1.5)
        );
    }

    @ParameterizedTest
    @MethodSource("provideNullStartOrNullEnd")
    public void givenNullStartOrNullEnd_whenGetRating_throwsIllegalArgumentException(
            LocalDateTime start,
            LocalDateTime end
    ) {
        Task task = new Task(validProjectName, validTaskName, start, end, true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, task::getRating);
        assertEquals("Task must have non-null start and end",  exception.getMessage());
    }

    @Test
    public void givenEndBeforeStart_whenGetRating_throwsIllegalArgumentException() {
        Task task = new Task(validProjectName, validTaskName, validStart.plusHours(1), validStart, true);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, task::getRating);
        assertEquals("Invalid time range: start must be before end",  exception.getMessage());
    }

    @Test
    public void givenTask_whenGetStart_returnStart() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        LocalDateTime start = task.getStart();
        assertEquals(validStart, start);
    }

    @Test
    public void givenTask_whenGetEnd_returnEnd() {
        Task task = new Task(validProjectName, validTaskName, validStart, validStart.plusHours(8), true);
        LocalDateTime end = task.getEnd();
        assertEquals(validStart.plusHours(8), end);
    }
}
