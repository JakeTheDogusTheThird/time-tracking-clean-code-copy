package org.example.service;

import org.example.model.TimeTracking;

import java.time.temporal.ChronoUnit;

public class TimeTrackingEvaluator implements Evaluator<TimeTracking> {
  private static final int MINIMUM_VALID_DURATION = 15;
  private static final int MAXIMUM_VALID_DURATION = 7 * 60;

  private final TaskEvaluator taskEvaluator;

  public TimeTrackingEvaluator(TaskEvaluator taskEvaluator) {
    this.taskEvaluator = taskEvaluator;
  }

  @Override
  public boolean isGood(TimeTracking timeTracking) {
    return hasValidTimes(timeTracking)
        && isChronologicallyValid(timeTracking)
        && taskEvaluator.isGood(timeTracking.getTask());
  }

  private boolean hasValidTimes(TimeTracking timeTracking) {
    return timeTracking.getTimeFrom() != null
        && timeTracking.getTimeTo() != null;
  }

  private boolean isChronologicallyValid(TimeTracking timeTracking) {
    long duration = ChronoUnit.MINUTES.between(timeTracking.getTimeFrom(), timeTracking.getTimeTo());
    return duration >= MINIMUM_VALID_DURATION
        && duration <= MAXIMUM_VALID_DURATION;
  }
}
