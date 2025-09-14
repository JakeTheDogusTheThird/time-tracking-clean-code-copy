package org.example.service;

import org.example.model.Task;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class TaskRankCalculator implements RankCalculator<Task> {
    public static final double WORK_EFFECTIVE              =  1.0;
    public static final double WORK_INEFFECTIVE            =  0.0;
    public static final double ESTIMATION_EXCEEDING_FACTOR = 1.5;

    @Override
    public double calculateRank(Task task) {
        Objects.requireNonNull(task, "task must not be null");

        LocalDateTime start = task.getStart();
        LocalDateTime end   = task.getEnd();
        double estimation   = task.getEstimationInHours();

        requireNotNullTimes(start, end);

        double hours      = calculateHours(start, end, estimation);
        double efficiency = task.isCompleted() ? WORK_EFFECTIVE : WORK_INEFFECTIVE;

        return efficiency - hours / estimation;
    }

    private void requireNotNullTimes(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new NullPointerException("Task must have non-null start and end");
        }
    }

    private double calculateHours(LocalDateTime start, LocalDateTime end, double estimation) {
        long hours = ChronoUnit.HOURS.between(start, end);
        if (hours < 0) {
            throw new IllegalArgumentException("Invalid time range: start must be before end");
        }
        if (hours > estimation * ESTIMATION_EXCEEDING_FACTOR) {
            throw new IllegalArgumentException("Task duration surpassed estimation by far");
        }
        return hours;
    }
}
