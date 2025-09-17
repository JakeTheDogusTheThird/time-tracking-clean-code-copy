package org.example.repository;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Person;
import org.example.service.PersonValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class JdbcPersonDao implements Dao<Person>{
  private final OracleXeDataSource dataSource;
  private final PersonValidator validator;

  public JdbcPersonDao(OracleXeDataSource dataSource, PersonValidator validator) {
    this.dataSource = dataSource;
    this.validator = validator;
  }

  @Override
  public Optional<Person> get(int id) {
    return Optional.empty();
  }

  @Override
  public List<Person> getAll() {
    return List.of();
  }

  @Override
  public void save(Person person) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = "INSERT INTO persons (id, name, phone) VALUES (?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
          if (validator.isValid(person)) {
            populateStatement(person, statement);
            statement.execute();
          }
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        throw new DataAccessException(e.getMessage(), e);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e.getMessage(), e);
    }
  }

  private void populateStatement(Person person, PreparedStatement statement) throws SQLException {
    statement.setInt(1, person.getId());
    statement.setString(2, person.getName());
    statement.setString(3, person.getPhone());
  }

  @Override
  public void saveAll(List<Person> persons) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = "INSERT INTO persons (id, name, phone) VALUES (?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        for (Person person : persons) {
          if (validator.isValid(person)) {
            populateStatement(person, statement);
            statement.addBatch();
          }
        }
        statement.executeBatch();
        connection.commit();
      } catch (SQLException e) {
        connection.rollback();
        throw new DataAccessException(e.getMessage(), e);
      }
    } catch (SQLException e) {
      throw new DataAccessException(e.getMessage(), e);
    }
  }
}
