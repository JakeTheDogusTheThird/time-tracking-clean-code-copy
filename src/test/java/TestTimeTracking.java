import org.example.model.Person;
import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;
import org.example.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TestTimeTracking {
  private static final LocalDateTime presenceTimeIn = LocalDateTime.of(2025, 9, 9, 9, 0);
  private static final LocalDateTime presenceTimeOut = LocalDateTime.of(2025, 9, 9, 19, 30);
  private static final Presence validPresence = new Presence(new Person("john", "+37312345678"), presenceTimeIn, presenceTimeOut);
  private static final Presence invalidPresence = new Presence(null, null, null);
  private static final LocalDateTime taskStart = LocalDateTime.of(2025, 9, 9, 10, 20);
  private static final LocalDateTime taskEnd = LocalDateTime.of(2025, 9, 9, 13, 30);
  private static final Task task3Hour10Minutes = new Task("project", "valid", taskStart, taskEnd, 10.0, true);
  private static final Task task8Hour30Minutes = new Task("project", "8 hour 30 minute task", taskStart, taskStart.plusHours(8).plusMinutes(30), 10.0, true);
  private static final Task invalidTask = new Task(null, null, null, null, 0.0, false);

  @Test
  public void givenValidTimeTracking_whenIsValid_returnTrue() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );

    PersonValidator personValidator = new PersonValidator();
    PresenceValidator presenceValidator = new PresenceValidator(personValidator);
    TaskValidator taskValidator = new TaskValidator();
    TimeTrackingValidator timeTrackingValidator = new TimeTrackingValidator(presenceValidator, taskValidator);

    boolean result = timeTrackingValidator.isValid(timeTracking);
    assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("provideTimeTrackingForIsValid")
  public void isValid_ShouldReturnFalseForInvalidTimeTracking(TimeTracking timeTracking) {
    PersonValidator personValidator = new PersonValidator();
    PresenceValidator presenceValidator = new PresenceValidator(personValidator);
    TaskValidator taskValidator = new TaskValidator();
    TimeTrackingValidator timeTrackingValidator = new TimeTrackingValidator(presenceValidator, taskValidator);

    boolean result = timeTrackingValidator.isValid(timeTracking);
    assertFalse(result);
  }

  private static Stream<TimeTracking> provideTimeTrackingForIsValid() {
    return Stream.of(
        new TimeTracking(null, task3Hour10Minutes, presenceTimeIn.minusHours(1), presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, null, presenceTimeIn.minusHours(1), presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, null, presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, presenceTimeIn.minusHours(1), null),
        new TimeTracking(invalidPresence, task3Hour10Minutes, presenceTimeIn.minusHours(1), presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, invalidTask, presenceTimeIn.minusHours(1), presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, presenceTimeIn.minusHours(1), presenceTimeOut.minusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, presenceTimeIn.plusHours(1), presenceTimeOut.plusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, taskStart.minusHours(1), taskEnd.minusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, taskStart.plusHours(1), taskEnd.plusHours(1)),
        new TimeTracking(validPresence, task3Hour10Minutes, presenceTimeOut.minusHours(1), presenceTimeIn.minusHours(1))
    );
  }

  @Test
  public void givenTimeEntryLessThan15Minutes_whenIsGood_returnFalse() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        taskStart.plusHours(1),
        taskStart.plusHours(1).plusMinutes(14));
    TaskEvaluator taskEvaluator = new TaskEvaluator();
    TimeTrackingEvaluator evaluator = new TimeTrackingEvaluator(taskEvaluator);
    boolean result = evaluator.isGood(timeTracking);
    assertFalse(result);
  }

  @Test
  public void givenTimeEntryBiggerThan7Hours_whenIsGood_returnFalse() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task8Hour30Minutes,
        taskStart.plusMinutes(1),
        taskStart.plusHours(8));
    TaskEvaluator taskEvaluator = new TaskEvaluator();
    TimeTrackingEvaluator evaluator = new TimeTrackingEvaluator(taskEvaluator);
    boolean result = evaluator.isGood(timeTracking);
    assertFalse(result);
  }

  @Test
  public void givenTaskWithNotGoodStatus_whenIsGood_returnFalse() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task8Hour30Minutes,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );
    TaskEvaluator taskEvaluator = new TaskEvaluator();
    TimeTrackingEvaluator evaluator = new TimeTrackingEvaluator(taskEvaluator);
    boolean result = evaluator.isGood(timeTracking);
    assertFalse(result);
  }

  @ParameterizedTest
  @MethodSource("provideTomeFromNullOrTimeInNull")
  public void givenTimeFromNullOrTimeInNull_whenIsGood_returnFalse(
      LocalDateTime timeFrom,
      LocalDateTime timeTo
  ) {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        timeFrom,
        timeTo
    );
    TaskEvaluator taskEvaluator = new TaskEvaluator();
    TimeTrackingEvaluator evaluator = new TimeTrackingEvaluator(taskEvaluator);
    boolean result = evaluator.isGood(timeTracking);
    assertFalse(result);
  }

  private static Stream<Arguments> provideTomeFromNullOrTimeInNull() {
    return Stream.of(
        Arguments.of(null, null),
        Arguments.of(taskStart.plusMinutes(1), null),
        Arguments.of(null, taskEnd.minusMinutes(1))
    );
  }

  @Test
  public void givenAllConditionsMet_whenIsGood_returnTrue() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );
    TaskEvaluator taskEvaluator = new TaskEvaluator();
    TimeTrackingEvaluator evaluator = new TimeTrackingEvaluator(taskEvaluator);
    boolean result = evaluator.isGood(timeTracking);
    assertTrue(result);
  }

  @ParameterizedTest
  @MethodSource("provideNullTimeFromOrNullTimeTo")
  public void givenNullTimeFromOrNullTimeTo_whenCalculateRank_throwsNullPointerException(
      LocalDateTime timeFrom,
      LocalDateTime timeTo
  ) {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        timeFrom,
        timeTo
    );

    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);
    assertThrows(NullPointerException.class, () -> rankCalculator.calculateRank(timeTracking));
  }

  private static Stream<Arguments> provideNullTimeFromOrNullTimeTo() {
    return Stream.of(
        Arguments.of(null, taskEnd.minusMinutes(1)),
        Arguments.of(taskStart.plusMinutes(1), null),
        Arguments.of(null, null)
    );
  }

  @ParameterizedTest
  @MethodSource("provideNullPresenceOrNullTask")
  public void givenNullPresenceOrNullTask_whenCalculateRank_throwsNullPointerException(
      Presence presence,
      Task task
  ) {
    TimeTracking timeTracking = new TimeTracking(
        presence,
        task,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );
    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);
    assertThrows(NullPointerException.class, () -> rankCalculator.calculateRank(timeTracking));
  }

  private static Stream<Arguments> provideNullPresenceOrNullTask() {
    return Stream.of(
        Arguments.of(null, task3Hour10Minutes),
        Arguments.of(validPresence, null),
        Arguments.of(null, null)
    );
  }

  @ParameterizedTest
  @MethodSource("providePresenceWithNullTimeInOrNullTimeOut")
  public void givenPresenceWithNullTimeInOrNullTimeOut_whenCalculateRank_throwsNullPointerException(
      Presence presence
  ) {
    TimeTracking timeTracking = new TimeTracking(
        presence,
        task3Hour10Minutes,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );
    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);
    assertThrows(NullPointerException.class, () -> rankCalculator.calculateRank(timeTracking));
  }

  private static Stream<Presence> providePresenceWithNullTimeInOrNullTimeOut() {
    return Stream.of(
        new Presence(new Person(null, null), null, presenceTimeOut),
        new Presence(new Person(null, null), presenceTimeIn, null),
        new Presence(new Person(null, null), null, null)
    );
  }

  @Test
  public void givenTimeFromBeforeTimeTo_whenCalculateRank_throwsIllegalArgumentException() {
    TimeTracking timeTracking = new TimeTracking(
        validPresence,
        task3Hour10Minutes,
        taskEnd.minusMinutes(1),
        taskStart.plusMinutes(1)
    );
    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);
    assertThrows(IllegalArgumentException.class, () -> rankCalculator.calculateRank(timeTracking));
  }

  @Test
  public void givenPresenceTimeInBeforeTimeOut_whenCalculateRank_throwsIllegalArgumentException() {
    TimeTracking timeTracking = new TimeTracking(
        new Presence(null, presenceTimeOut, presenceTimeIn),
        task3Hour10Minutes,
        taskStart.plusMinutes(1),
        taskEnd.minusMinutes(1)
    );
    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);
    assertThrows(IllegalArgumentException.class, () -> rankCalculator.calculateRank(timeTracking));
  }

  @ParameterizedTest
  @MethodSource("provideRankCases")
  public void givenTimeTracking_whenCalculateRank_returnRank(
      TimeTracking timeTracking,
      double expectedResult
  ) {
    TaskRankCalculator taskRankCalculator = new TaskRankCalculator();
    TimeTrackingRankCalculator rankCalculator = new TimeTrackingRankCalculator(taskRankCalculator);

    double result = rankCalculator.calculateRank(timeTracking);
    double errorMargin = 0.001;
    assertEquals(expectedResult, result, errorMargin);
  }

  public static Stream<Arguments> provideRankCases() {
    return Stream.of(
        Arguments.of(
            new TimeTracking(
                validPresence,
                task3Hour10Minutes,
                taskStart.plusMinutes(1),
                taskEnd.minusMinutes(1)),
            0.509
        ),
        Arguments.of(
            new TimeTracking(
                validPresence,
                task8Hour30Minutes,
                taskStart.plusMinutes(1),
                taskStart.plusHours(8).plusMinutes(29)
            ),
            0.312
        ),
        Arguments.of(
            new TimeTracking(
                validPresence,
                task8Hour30Minutes,
                taskStart.plusMinutes(1),
                taskStart.plusMinutes(14)
            ),
            0.026
        )
    );
  }
}
