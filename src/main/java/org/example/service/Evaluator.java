package org.example.service;

public interface Evaluator<T> {
  boolean isGood(T entity);
}