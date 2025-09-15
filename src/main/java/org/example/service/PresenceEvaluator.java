package org.example.service;

import org.example.model.Presence;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class PresenceEvaluator implements Evaluator<Presence> {
  private final static double MINUTES_IN_HOUR = 60.0;
  private final static LocalTime TIME_BEFORE_PENALTY = LocalTime.of(9, 15);
  private final static int MINIMUM_NORMAL_WORK_DAY = 8;
  private final static int MAXIMUM_NORMAL_WORK_DAY = 9;

  @Override
  public boolean isGood(Presence presence) {
    LocalDateTime in = presence.getTimeIn();
    LocalDateTime out = presence.getTimeOut();

    if (in == null || out == null)
      return false;

    double hours = ChronoUnit.MINUTES.between(in, out) / MINUTES_IN_HOUR;

    return hours >= MINIMUM_NORMAL_WORK_DAY
        && hours <= MAXIMUM_NORMAL_WORK_DAY
        && in.toLocalTime().isBefore(TIME_BEFORE_PENALTY);
  }
}
