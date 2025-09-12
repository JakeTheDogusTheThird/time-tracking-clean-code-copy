package org.example.service;

import org.example.model.Task;

public interface TaskValidator {
  boolean isValid(Task task);
}
