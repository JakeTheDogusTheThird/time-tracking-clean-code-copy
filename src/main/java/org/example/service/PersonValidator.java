package org.example.service;

import org.example.model.Person;

import java.util.regex.Pattern;

public interface PersonValidator {
  boolean isValid(Person person);
}
