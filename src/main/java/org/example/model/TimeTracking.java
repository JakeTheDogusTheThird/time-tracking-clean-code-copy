package org.example.model;

import lombok.Getter;

import java.time.LocalDateTime;

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
}
