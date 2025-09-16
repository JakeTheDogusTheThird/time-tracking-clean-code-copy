package org.example.service;

import org.example.model.Presence;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class PresenceRankCalculator implements RankCalculator<Presence> {
  private static final double MINUTES_IN_HOUR = 60;
  private static final double NORMAL_WORK_DAY = 8;
  private static final LocalTime START_OF_DAY = LocalTime.of(9, 1);
  private static final LocalTime PUNCTUALITY_LIMIT = LocalTime.of(9, 15);
  private static final double MAX_PUNCTUALITY_FACTOR = 0.5;
  private static final double PUNCTUALITY_ATTENUATION_FACTOR = 30.0;

  @Override
  public double calculateRank(Presence presence) {
    Objects.requireNonNull(presence, "presence must not be null");

    LocalDateTime in = presence.getTimeIn();
    LocalDateTime out = presence.getTimeOut();
    requireNonNullTimes(in, out);

    double hours = calculateHours(in, out);
    double punctuality = calculatePunctuality(in);
    return (hours / NORMAL_WORK_DAY) + punctuality;
  }

  private void requireNonNullTimes(LocalDateTime in, LocalDateTime out) {
    if (in == null || out == null) {
      throw new NullPointerException("Presence must have non-null timeIn and timeOut");
    }
  }

  private double calculateHours(LocalDateTime in, LocalDateTime out) {
    double hours = ChronoUnit.MINUTES.between(in, out);
    if (hours < 0) {
      throw new IllegalArgumentException("Presence must have timeIn before timeOut");
    }
    return hours / MINUTES_IN_HOUR;
  }

  private double calculatePunctuality(LocalDateTime in) {
    if (in.toLocalTime().isBefore(START_OF_DAY)) {
      return MAX_PUNCTUALITY_FACTOR;
    } else if (in.toLocalTime().isBefore(PUNCTUALITY_LIMIT)) {
      return MAX_PUNCTUALITY_FACTOR - in.getMinute() / PUNCTUALITY_ATTENUATION_FACTOR;
    }
    return 0.0;
  }
}
