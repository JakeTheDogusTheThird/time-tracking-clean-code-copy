package org.example.service;

import org.example.model.Person;
import org.example.model.Presence;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class PresenceValidator implements Validator<Presence> {
  private static final LocalTime START_DAY = LocalTime.of(7, 29);
  private static final LocalTime END_DAY   = LocalTime.of(21, 31);

  private final PersonValidator personValidator;

  public PresenceValidator(PersonValidator personValidator) {
    this.personValidator = personValidator;
  }

  @Override
  public boolean isValid(Presence presence) {
    if (presence == null) {
      return false;
    }

    Person person = presence.getPerson();
    LocalDateTime in = presence.getTimeIn();
    LocalDateTime out = presence.getTimeOut();

    return this.personValidator.isValid(person)
        && isValidCheckIn(in)
        && isValidCheckOut(out)
        && isChronologicallyValid(in, out);
  }

  private boolean isValidCheckIn(LocalDateTime in) {
    return in != null && in.toLocalTime().isAfter(START_DAY);
  }

  private boolean isValidCheckOut(LocalDateTime out) {
    return out != null && out.toLocalTime().isBefore(END_DAY);
  }

  private boolean isChronologicallyValid(LocalDateTime in, LocalDateTime out) {
    return in != null && out != null && in.isBefore(out);
  }
}
