package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class TimeTracking {
  @Setter
  private int id;
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
