package org.example.service;

import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;

import java.time.LocalDateTime;

public class TimeTrackingValidator implements Validator<TimeTracking> {
  private final PresenceValidator presenceValidator;
  private final TaskValidator taskValidator;

  public TimeTrackingValidator(PresenceValidator presenceValidator, TaskValidator taskValidator) {
    this.presenceValidator = presenceValidator;
    this.taskValidator = taskValidator;
  }

  public boolean isValid(TimeTracking timeTracking) {
    Presence presence = timeTracking.getPresence();
    Task task = timeTracking.getTask();
    LocalDateTime timeFrom = timeTracking.getTimeFrom();
    LocalDateTime timeTo = timeTracking.getTimeTo();

    return presence != null
        && this.presenceValidator.isValid(presence)
        && this.taskValidator.isValid(task)
        && isChronologicallyValid(timeFrom, timeTo)
        && isTimeRangeWithinPresence(timeFrom, timeTo, presence)
        && isTimeRangeWithinTaskTimeRange(timeFrom, timeTo, task);
  }

  private boolean isChronologicallyValid(LocalDateTime timeFrom, LocalDateTime timeTo) {
    return timeFrom != null && timeTo != null && timeFrom.isBefore(timeTo);
  }

  private boolean isTimeRangeWithinPresence(LocalDateTime timeFrom, LocalDateTime timeTo, Presence presence) {
    return timeFrom != null
        && timeTo != null
        && timeFrom.isAfter(presence.getTimeIn())
        && timeTo.isBefore(presence.getTimeOut());
  }

  private boolean isTimeRangeWithinTaskTimeRange(LocalDateTime timeFrom, LocalDateTime timeTo, Task task) {
    return timeFrom != null
        && timeTo != null
        && timeFrom.isAfter(task.getStart())
        && timeTo.isBefore(task.getEnd());
  }
}
