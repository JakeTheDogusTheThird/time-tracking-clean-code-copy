package org.example.repository;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Task;
import org.example.service.TaskValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JdbcTaskDao implements Dao<Task> {
  private final OracleXeDataSource dataSource;
  private final TaskValidator validator;

  public JdbcTaskDao(OracleXeDataSource dataSource, TaskValidator validator) {
    this.dataSource = dataSource;
    this.validator = validator;
  }

  @Override
  public Optional<Task> get(int id) {
    return Optional.empty();
  }

  @Override
  public List<Task> getAll() {
    return List.of();
  }

  @Override
  public void save(Task task) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = """
          INSERT INTO tasks (id, project, name, start, end, estimation_in_hours, completed)
          VALUES (?, ?, ?, ?, ?, ?, ?)
          """;
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        if (validator.isValid(task)) {
          buildStatement(statement, task);
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

  @Override
  public void saveAll(List<Task> tasks) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = """
          INSERT INTO tasks (id, project, name, start, end, estimation_in_hours, completed)
          VALUES (?, ?, ?, ?, ?, ?, ?)
          """;
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        for (Task task : tasks) {
          if (validator.isValid(task)) {
            buildStatement(statement, task);
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

  private void buildStatement(PreparedStatement statement, Task task) throws SQLException {
    statement.setInt(1, task.getId());
    statement.setString(2, task.getProject());
    statement.setString(3, task.getName());
    statement.setTimestamp(4, Timestamp.valueOf(task.getStart()));
    statement.setTimestamp(5, Timestamp.valueOf(task.getEnd()));
    statement.setDouble(6, task.getEstimationInHours());
    statement.setBoolean(7, task.isCompleted());
  }
}
