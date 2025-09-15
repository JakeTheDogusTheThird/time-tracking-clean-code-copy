package org.example.service;

import org.example.model.Task;
import java.time.temporal.ChronoUnit;

public class TaskEvaluator implements Evaluator<Task> {
    private static final int MINUTES_IN_HOUR = 60;
    private static final int WORK_DAY = 8;
    private static final int MINIMUM_VALID_DURATION = 0;
    @Override
    public boolean isGood(Task task) {
        return hasValidTimes(task) && isChronologicallyValid(task)
                && task.isCompleted();
    }

    private boolean hasValidTimes(Task task) {
        return task.getStart() != null && task.getEnd() != null;
    }

    private boolean isChronologicallyValid(Task task) {
        double duration = (double) ChronoUnit.MINUTES.between(task.getStart(), task.getEnd())
                / MINUTES_IN_HOUR;
        return duration >= MINIMUM_VALID_DURATION && duration <= WORK_DAY;
    }
}

