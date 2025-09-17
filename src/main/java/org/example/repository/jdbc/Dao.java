package org.example.repository.jdbc;

import org.example.repository.exceptions.DataAccessException;

import java.util.List;

public interface Dao<T> {
  void saveAll(List<T> ts) throws DataAccessException;
}
