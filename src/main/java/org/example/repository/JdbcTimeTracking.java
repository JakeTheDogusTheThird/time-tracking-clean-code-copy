package org.example.repository;

import org.example.datasources.OracleXeDataSource;
import org.example.model.TimeTracking;
import org.example.service.TimeTrackingValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JdbcTimeTracking implements Dao<TimeTracking>{
  private final OracleXeDataSource dataSource;
  private final TimeTrackingValidator validator;

  public JdbcTimeTracking(OracleXeDataSource dataSource, TimeTrackingValidator validator) {
    this.dataSource = dataSource;
    this.validator = validator;
  }

  @Override
  public Optional<TimeTracking> get(int id) {
    return Optional.empty();
  }

  @Override
  public List<TimeTracking> getAll() {
    return List.of();
  }

  @Override
  public void save(TimeTracking timeTracking) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = """
          INSERT INTO tasks (id, presence_id, task_id, time_from, time_to)
          VALUES (?, ?, ?, ?, ?)
          """;
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
          if (validator.isValid(timeTracking)) {
            buildStatement(statement, timeTracking);
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
  public void saveAll(List<TimeTracking> timeTrackings) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = """
          INSERT INTO tasks (id, presence_id, task_id, time_from, time_to)
          VALUES (?, ?, ?, ?, ?)
          """;
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        for (TimeTracking timeTracking : timeTrackings) {
          if (validator.isValid(timeTracking)) {
            buildStatement(statement, timeTracking);
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

  private void buildStatement(PreparedStatement statement, TimeTracking timeTracking) throws SQLException {
    statement.setInt(1, timeTracking.getId());
    statement.setInt(2, timeTracking.getPresence().getId());
    statement.setInt(3, timeTracking.getTask().getId());
    statement.setTimestamp(4, Timestamp.valueOf(timeTracking.getTimeFrom()));
    statement.setTimestamp(5, Timestamp.valueOf(timeTracking.getTimeTo()));
  }
}
