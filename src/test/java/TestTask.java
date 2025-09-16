import org.example.model.Task;
import org.example.service.TaskEvaluator;
import org.example.service.TaskRankCalculator;
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
    Task task = new Task(
        projectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
    boolean result = taskValidator.isValid(task);
    assertFalse(result);
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  @ValueSource(strings = {" ", "\t", "\n", "\u000B", "\f", "\r"})
  public void givenBlankTaskName_whenIsValid_returnFalse(String taskName) {
    Task task = new Task(
        validProjectName,
        taskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
    boolean result = taskValidator.isValid(task);
    assertFalse(result);
  }

  @Test
  public void givenValidProjectName_whenIsValid_returnTrue() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
    boolean result = taskValidator.isValid(task);
    assertTrue(result);
  }

  @Test
  public void givenValidTaskName_whenIsValid_returnTrue() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
    boolean result = taskValidator.isValid(task);
    assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("provideInvalidTimeEntries")
  public void isValid_ShouldReturnFalseForNonValidTimeEntries(LocalDateTime start, LocalDateTime end) {
    Task task = new Task(
        validProjectName,
        validTaskName,
        start,
        end,
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
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
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskValidator taskValidator = new TaskValidator();
    boolean result = taskValidator.isValid(task);
    assertTrue(result);
  }

  @Test
  public void givenNotCompletedTask_whenIsGood_returnFalse() {
    Task task = new Task(validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        false);
    TaskEvaluator evaluator = new TaskEvaluator();
    boolean result = evaluator.isGood(task);
    assertFalse(result);
  }

  @Test
  public void givenOverEightHoursWorkedTime_whenIsGood_returnFalse() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(9),
        10.0,
        true);
    TaskEvaluator evaluator = new TaskEvaluator();
    boolean result = evaluator.isGood(task);
    assertFalse(result);
  }

  @Test
  public void givenEightHoursWorkedTime_whenIsGood_returnTrue() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart,
        validStart.plusHours(8),
        10.0,
        true);
    TaskEvaluator evaluator = new TaskEvaluator();
    boolean result = evaluator.isGood(task);
    assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("provideNullStartOrNullEnd")
  public void givenNullStartOrNullEnd_whenIsGood_returnFalse(
      LocalDateTime start,
      LocalDateTime end
  ) {
    Task task = new Task(
        validProjectName,
        validTaskName,
        start,
        end,
        10.0,
        true);
    TaskEvaluator evaluator = new TaskEvaluator();
    boolean result = evaluator.isGood(task);
    assertFalse(result);
  }

  @Test
  public void givenEndBeforeStart_whenIsGood_returnFalse() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart.plusHours(1),
        validStart,
        10.0,
        true);
    TaskEvaluator evaluator = new TaskEvaluator();
    boolean result = evaluator.isGood(task);
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
  @MethodSource("provideRankCases")
  public void givenRankCases_whenCalculateRank_returnRank(
      LocalDateTime timeIn,
      LocalDateTime timeOut,
      boolean completed,
      double expected
  ) {
    double errorMargin = 0.001;
    Task task = new Task(
        validProjectName,
        validTaskName,
        timeIn,
        timeOut,
        10.0,
        completed);
    TaskRankCalculator rankCalculator = new TaskRankCalculator();
    double result = rankCalculator.calculateRank(task);
    assertEquals(expected, result, errorMargin);
  }

  private static Stream<Arguments> provideRankCases() {
    // start, end, completed, expected result
    return Stream.of(
        Arguments.of(validStart, validStart.plusHours(8), true, 0.2),
        Arguments.of(validStart, validStart.plusHours(8), false, -0.8),
        Arguments.of(validStart, validStart.plusHours(4), true, 0.6),
        Arguments.of(validStart, validStart.plusHours(4), false, -0.4)
    );
  }

  @ParameterizedTest
  @MethodSource("provideNullStartOrNullEnd")
  public void givenNullStartOrNullEnd_whenCalculateRank_throwsIllegalArgumentException(
      LocalDateTime start,
      LocalDateTime end
  ) {
    Task task = new Task(
        validProjectName,
        validTaskName,
        start,
        end,
        10.0,
        true);
    TaskRankCalculator rankCalculator = new TaskRankCalculator();
    NullPointerException exception = assertThrows(NullPointerException.class,
        () -> rankCalculator.calculateRank(task));
    assertEquals("Task must have non-null start and end", exception.getMessage());
  }

  @Test
  public void givenEndBeforeStart_whenCalculateRank_throwsIllegalArgumentException() {
    Task task = new Task(
        validProjectName,
        validTaskName,
        validStart.plusHours(1),
        validStart,
        10.0,
        true);
    TaskRankCalculator rankCalculator = new TaskRankCalculator();
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
        () -> rankCalculator.calculateRank(task));
    assertEquals("Invalid time range: start must be before end", exception.getMessage());
  }
}
