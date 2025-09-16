package org.example.model;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Presence {
  private int id;
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
}
