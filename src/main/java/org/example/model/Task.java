package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Task {
  @Setter
  private int id;
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
}
