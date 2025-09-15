package org.example.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class Task {
  private final String project;
  private final String name;
  private final LocalDateTime start;
  private final LocalDateTime end;
  private final double estimationInHours;
  private final boolean completed;

  public Task(
      String project,
      String name,
      LocalDateTime start,
      LocalDateTime end,
      double estimationInHours,
      boolean completed
  ) {
    this.project = project;
    this.name = name;
    this.start = start;
    this.end = end;
    this.estimationInHours = estimationInHours;
    this.completed = completed;
  }

  public boolean isGood() {
    if (this.start == null || this.end == null) {
      return false;
    }

    double duration = ChronoUnit.MINUTES.between(this.start, this.end) / 60.0;
    if (duration < 0) {
      return false;
    }

    return this.completed && duration <= 8;
  }
}
