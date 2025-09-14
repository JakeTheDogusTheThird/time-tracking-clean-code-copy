package org.example.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public class Presence {
  private final Person person;
  private final LocalDateTime timeIn;
  private final LocalDateTime timeOut;

  public Presence(
      Person person,
      LocalDateTime timeIn,
      LocalDateTime timeOut
  ) {
    this.person = person;
    this.timeIn = timeIn;
    this.timeOut = timeOut;
  }

  public boolean isGood() {
    if (this.timeIn == null || this.timeOut == null)
      return false;

    double hours = ChronoUnit.MINUTES.between(this.timeIn, this.timeOut) / 60.0;

    return hours >= 8
        && hours <= 9
        && this.timeIn.toLocalTime().isBefore(LocalTime.of(9, 15));
  }
}
