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

  public double getRating() {
    if (this.start == null || this.end == null) {
      throw new NullPointerException("Task must have non-null start and end");
    }

    double duration = ChronoUnit.HOURS.between(this.start, this.end);
    if (duration < 0) {
      throw new IllegalArgumentException("Invalid time range: start must be before end");
    }

    if (duration > estimationInHours * 1.5) {
        throw new IllegalArgumentException("Task duration overly surpassed estimation");
    }

    double efficiency = this.completed ? 1.0 : 0.0;

    return efficiency - duration / estimationInHours;
  }
}
