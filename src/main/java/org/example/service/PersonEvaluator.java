package org.example.service;

import org.example.model.Person;

import java.util.Objects;

public class PersonEvaluator implements Evaluator<Person>{
  private static final String PHONE_COUNTRY_CODE = "+373";

  @Override
  public boolean isGood(Person person) {
    String phone =  person.getPhone();
    boolean phoneIsNull = Objects.isNull(phone);
    return !phoneIsNull && phone.startsWith(PHONE_COUNTRY_CODE);
  }
}
