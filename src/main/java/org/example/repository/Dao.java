package org.example.repository;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {
  Optional<T> get(int id);

  List<T> getAll();

  T save(T t);

  T update(int id, T t);

  void delete(int id);
}
