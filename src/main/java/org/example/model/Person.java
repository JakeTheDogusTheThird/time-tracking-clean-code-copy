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

  public boolean isGood() {
    if (this.phone == null) {
      return false;
    }
    return this.phone.startsWith("+373");
  }
}
