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
}
