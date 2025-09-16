package org.example.repository;

import org.example.model.Person;

import java.util.List;
import java.util.Optional;

public interface ReaderToEntityMapper <T> {
  public T get(int id);
  public List<T> getAll();
}
