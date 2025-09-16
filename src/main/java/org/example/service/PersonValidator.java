package org.example.service;

import org.example.model.Person;

import java.util.regex.Pattern;

public class PersonValidator implements Validator<Person> {
  private static final Pattern PHONE_PATTERN =
      Pattern.compile("^\\+373[0-9]{8}$|^\\+39[0-9]{6,12}$");
  private static final Pattern NAME_PATTERN =
      Pattern.compile("(\\p{Alpha} ?)*");

  @Override
  public boolean isValid(Person person) {
    if (person == null) {
      return false;
    }

    String name = person.getName();
    String phone = person.getPhone();

    return isValidPersonName(name) && isValidItalianOrMoldovanPhoneNumber(phone);
  }

  private boolean isValidItalianOrMoldovanPhoneNumber(String phone) {
    return phone != null
        && !phone.isBlank()
        && PHONE_PATTERN.matcher(phone).matches();
  }

  private boolean isValidPersonName(String name) {
    return name != null
        && !name.isBlank()
        && NAME_PATTERN.matcher(name).matches();
  }
}
