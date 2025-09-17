package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
  Optional<T> get(int id);

  List<T> getAll();

  void save(T t) throws DataAccessException;

  void saveAll(List<T> ts) throws DataAccessException;
}
