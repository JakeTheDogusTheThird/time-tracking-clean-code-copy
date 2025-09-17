package org.example.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class Presence {
  @Setter
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
