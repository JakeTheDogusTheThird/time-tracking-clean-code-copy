package org.example.service;

import org.example.model.Person;

import java.util.Objects;

public class PersonEvaluator implements Evaluator<Person>{
  private static final String COMFORTABLE_LANGUAGE_PHONE_NUMBER = "+373";

  @Override
  public boolean isGood(Person person) {
    String phone =  person.getPhone();
    Objects.requireNonNull(phone, "phone is null");
    return phone.startsWith(COMFORTABLE_LANGUAGE_PHONE_NUMBER);
  }
}
