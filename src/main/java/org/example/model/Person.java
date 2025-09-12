package org.example.model;


import lombok.Getter;

@Getter
public class Person {
  private final String name;
  private final String phone;

  public Person(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

  public boolean isValid() {
    return this.name != null
        && !this.name.isBlank()
        && this.name.matches("(\\p{Alpha} ?)*")
        && this.phone != null
        && !this.phone.isBlank()
        && this.phone.matches("^\\+373[0-9]{8}$|^\\+39[0-9]{6,12}$");
  }

  public boolean isGood() {
    if (this.phone == null) {
      return false;
    }
    return this.phone.startsWith("+373");
  }

  public long getRating() {
    if (this.phone == null) {
      throw new IllegalArgumentException("Phone must not bet null");
    }

    long phoneLongValue = Long.parseLong(this.phone.substring(1));
    switch (this.phone.length()) {
      case 12:
        phoneLongValue = phoneLongValue * 1_000;
        break;
      case 9:
        phoneLongValue = phoneLongValue * 1_000_000;
      default:
        break;
    }

    return phoneLongValue;
  }
}
