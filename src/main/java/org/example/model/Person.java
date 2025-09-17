package org.example.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
public class Person {
  @Setter
  private int id;
  private final String name;
  private final String phone;

  public Person(String name, String phone) {
    this.name = name;
    this.phone = phone;
  }

}
