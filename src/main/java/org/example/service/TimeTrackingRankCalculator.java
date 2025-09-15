package org.example.service;

import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TimeTrackingRankCalculator implements RankCalculator<TimeTracking>  {
  private static final int MINIMUM_DURATION = 15;
  private static final int MAX_DURATION = 7 * 60;
  private static final double TASK_WEIGHT = 0.6;
  private static final double PRESENCE_COVERAGE_WEIGHT = 0.3;
  private static final double PENALTY_WEIGHT = 0.1;
  private static final double NO_PENALTY = 0.0;
  private static final double UNDER_WORK_PENALTY = -1.0;
  private static final double OVER_WORK_PENALTY = -0.5;

  private final TaskRankCalculator taskRankCalculator;

  public TimeTrackingRankCalculator(TaskRankCalculator taskRankCalculator) {
    this.taskRankCalculator = taskRankCalculator;
  }

  @Override
  public double calculateRank(TimeTracking timeTracking) {
    LocalDateTime from = Objects.requireNonNull(timeTracking.getTimeFrom(), "timeFrom is required");
    LocalDateTime to = Objects.requireNonNull(timeTracking.getTimeTo(), "timeTo is required");
    Presence presence = Objects.requireNonNull(timeTracking.getPresence(), "presence is required");
    Task task = Objects.requireNonNull(timeTracking.getTask(), "task is required");
    LocalDateTime in = presence.getTimeIn();
    LocalDateTime out = presence.getTimeOut();

    requireInBeforeOut(from, to, "TimeFrom should be before TimeIn");
    requireInBeforeOut(in, out, "Presence: In should be before Out");

    long duration = ChronoUnit.MINUTES.between(from, to);
    double penalty = calculatePenalty(duration);
    double taskScore = this.taskRankCalculator.calculateRank(task);
    double presenceCoverage = (double) duration / ChronoUnit.MINUTES.between(in, out);

    return taskScore * TASK_WEIGHT + presenceCoverage * PRESENCE_COVERAGE_WEIGHT + penalty * PENALTY_WEIGHT;
  }

  private double calculatePenalty(long duration) {
    if (duration < MINIMUM_DURATION) {
      return UNDER_WORK_PENALTY;
    } else if (duration > MAX_DURATION) {
      return OVER_WORK_PENALTY;
    }
    return NO_PENALTY;
  }

  private void requireInBeforeOut(LocalDateTime in, LocalDateTime out, String exceptionMessage) {
    if (!in.isBefore(out)) {
      throw new IllegalArgumentException(exceptionMessage);
    }
  }
}
