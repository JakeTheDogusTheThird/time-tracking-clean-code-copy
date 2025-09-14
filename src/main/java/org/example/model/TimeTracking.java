package org.example.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class TimeTracking {
  private final Presence presence;
  private final Task task;
  private final LocalDateTime timeFrom;
  private final LocalDateTime timeTo;

  public TimeTracking(
      Presence presence,
      Task task,
      LocalDateTime timeFrom,
      LocalDateTime timeTo
  ) {
    this.presence = presence;
    this.task = task;
    this.timeFrom = timeFrom;
    this.timeTo = timeTo;
  }

  public boolean isGood() {
    if (this.timeFrom == null || this.timeTo == null) {
      return false;
    }

    long duration = ChronoUnit.MINUTES.between(this.timeFrom, this.timeTo);
    return this.task.isGood()
        && duration >= 15
        && duration <= 7 * 60;
  }

  public double getRating() {
    if (this.timeFrom == null
        || this.timeTo == null
        || this.presence == null
        || this.task == null
        || this.presence.getTimeIn() == null
        || this.presence.getTimeOut() == null) {
      throw new IllegalArgumentException("TimeTracking must have non-null presence,task, timeFrom and timeTo");
    }

    if (!this.timeFrom.isBefore(this.timeTo)
        || !this.presence.getTimeIn().isBefore(this.presence.getTimeOut())) {
      throw new IllegalArgumentException("""
          Invalid time range: timeFrom must be before timeTo AND presence timeIn should be before timeOut""");
    }

    long duration = ChronoUnit.MINUTES.between(this.timeFrom, this.timeTo);
    double penalty = 0.0;
    if (duration < 15) {
      penalty = -1.0;
    } else if (duration > 7 * 60) {
      penalty = -0.5;
    }

    double taskScore = this.task.getRating();
    double presenceCoverage = (double) duration
        / ChronoUnit.MINUTES.between(this.presence.getTimeIn(), this.presence.getTimeOut());

    return taskScore * 0.6 + presenceCoverage * 0.3 + (penalty * 0.1);
  }
}
