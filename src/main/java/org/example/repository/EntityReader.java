package org.example.repository;

import java.util.List;

public interface EntityReader<T> {
  public T get(int id) throws DataAccessException;
  public List<T> getAll() throws DataAccessException;
}
