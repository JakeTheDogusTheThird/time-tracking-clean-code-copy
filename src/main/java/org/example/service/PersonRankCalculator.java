package org.example.service;

import org.example.model.Person;

public class PersonRankCalculator implements RankCalculator<Person> {
  private static final int MOLDAVIAN_PHONE_NUMBER_LENGTH = 12;
  private static final int SHORT_ITALIAN_PHONE_NUMBER_LENGTH = 9;
  private static final int MOLDAVIAN_MULTIPLIER = 1_000;
  private static final int ITALIAN_MULTIPLIER = 1_000_000;

  @Override
  public double calculateRank(Person person) {
    String phone = validatePhone(person);
    long phoneLongValue = parsePhone(phone);
    return applyMultiplier(phoneLongValue, phone.length());
  }

  private String validatePhone(Person person) {
    String phone = person.getPhone();
    if (phone == null) {
      throw new IllegalArgumentException("Phone must not be null");
    }
    return phone;
  }

  private long parsePhone(String phone) {
    return Long.parseLong(phone.substring(1));
  }

  private long applyMultiplier(long phoneLongValue, int length) {
    return switch (length) {
      case MOLDAVIAN_PHONE_NUMBER_LENGTH -> phoneLongValue * MOLDAVIAN_MULTIPLIER;
      case SHORT_ITALIAN_PHONE_NUMBER_LENGTH -> phoneLongValue * ITALIAN_MULTIPLIER;
      default -> phoneLongValue;
    };
  }
}
