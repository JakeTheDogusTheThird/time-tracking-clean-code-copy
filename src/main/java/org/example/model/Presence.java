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

  public boolean isValid() {
    return this.person != null
        && this.person.isValid()
        && this.timeIn != null
        && this.timeOut != null
        && this.timeIn.toLocalTime().isAfter(LocalTime.of(7, 29))
        && this.timeOut.toLocalTime().isBefore(LocalTime.of(21, 30))
        && this.timeIn.isBefore(this.timeOut);
  }

  public boolean isGood() {
    if (this.timeIn == null || this.timeOut == null)
      return false;

    double hours = ChronoUnit.MINUTES.between(this.timeIn, this.timeOut) / 60.0;

    return hours >= 8
        && hours <= 9
        && this.timeIn.toLocalTime().isBefore(LocalTime.of(9, 15));
  }

  public double getRating() {
    if (this.timeIn == null || this.timeOut == null) {
      throw new IllegalArgumentException("Presence must have non-null timeIn and timeOut");
    }

    double hours = ChronoUnit.MINUTES.between(this.timeIn, this.timeOut) / 60.0;
    if (hours < 0) {
      throw new IllegalArgumentException("Presence must have timeIn before timeOut");
    }

    double punctuality = 0.0;
    if (this.timeIn.toLocalTime().isBefore(LocalTime.of(9, 1))) {
      punctuality = 0.5;
    } else if (this.timeIn.toLocalTime().isBefore(LocalTime.of(9, 15))) {
      punctuality = (15.0 - this.timeIn.getMinute()) / 30.0;
    }

    return (hours / 8.0) + punctuality;
  }
}
