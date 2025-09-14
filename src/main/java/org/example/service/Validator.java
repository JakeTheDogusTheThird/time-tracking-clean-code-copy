package org.example.service;

public interface Validator <T>{
  boolean isValid(T entity);
}
