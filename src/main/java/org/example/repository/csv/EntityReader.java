package org.example.repository.csv;

import org.example.repository.exceptions.DataAccessException;

import java.util.List;

public interface EntityReader<T> {
  T get(int id) throws DataAccessException;
  List<T> getAll() throws DataAccessException;
}
