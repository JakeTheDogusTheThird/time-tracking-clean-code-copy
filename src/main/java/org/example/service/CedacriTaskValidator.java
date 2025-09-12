package org.example.service;

import org.example.model.Task;
import java.time.LocalDateTime;

public class CedacriTaskValidator implements TaskValidator {
  public boolean isValid(Task task) {
    if (task == null) {
      return false;
    }

    String projectName = task.getProject();
    String taskName = task.getName();
    LocalDateTime start = task.getStart();
    LocalDateTime end = task.getEnd();

    return isValidProjectName(projectName)
        && isValidTaskName(taskName)
        && isChronologicallyValid(start, end);
  }


  private boolean isValidProjectName(String projectName) {
    return projectName != null
        && !projectName.isBlank();
  }

  private boolean isValidTaskName(String taskName) {
    return taskName != null
        && !taskName.isBlank();
  }

  private boolean isChronologicallyValid(LocalDateTime start, LocalDateTime end) {
    return start != null && end != null && start.isBefore(end);
  }
}
