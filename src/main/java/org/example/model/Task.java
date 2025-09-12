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
  private final boolean completed;

  public Task(
      String project,
      String name,
      LocalDateTime start,
      LocalDateTime end,
      boolean completed
  ) {
    this.project = project;
    this.name = name;
    this.start = start;
    this.end = end;
    this.completed = completed;
  }

  public boolean isValid() {
    return this.project != null
        && !this.project.isBlank()
        && this.name != null
        && !this.name.isBlank()
        && this.start != null
        && this.end != null
        && this.start.isBefore(this.end);
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
      throw new IllegalArgumentException("Task must have non-null start and end");
    }

    long duration = ChronoUnit.HOURS.between(this.start, this.end);
    if (duration < 0) {
      throw new IllegalArgumentException("Invalid time range: start must be before end");
    }

    double efficiency = this.completed ? 1.0 : -1.0;
    double durationFactor = 1.0;
    if (duration <= 7) {
      durationFactor += (1.0 - duration * 0.125);
    }

    return efficiency * durationFactor;
  }
}
