package org.example.repository;

import org.example.model.Person;

import java.util.List;
import java.util.Optional;

public class OracleXePersonDao implements Dao<Person>{
  @Override
  public Optional<Person> get(int id) {
    return Optional.empty();
  }

  @Override
  public List<Person> getAll() {
    return List.of();
  }

  @Override
  public Person save(Person person) {
    return null;
  }

  @Override
  public Person update(int id, Person person) {
    return null;
  }

  @Override
  public void delete(int id) {

  }
}
