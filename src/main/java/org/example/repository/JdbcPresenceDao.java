package org.example.repository;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Presence;
import org.example.service.PresenceValidator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

public class JdbcPresenceDao implements Dao<Presence> {
  private final OracleXeDataSource dataSource;
  private final PresenceValidator validator;

  public JdbcPresenceDao(OracleXeDataSource dataSource, PresenceValidator validator) {
    this.dataSource = dataSource;
    this.validator = validator;
  }

  @Override
  public Optional<Presence> get(int id) {
    return Optional.empty();
  }

  @Override
  public List<Presence> getAll() {
    return List.of();
  }

  @Override
  public void save(Presence presence) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = "INSERT INTO presences (id, person_id, time_in, time_out) VALUES (?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        if (validator.isValid(presence)) {
          populateStatement(presence, statement);
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
  public void saveAll(List<Presence> presences) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);

      String sql = "INSERT INTO presences (id, person_id, time_in, time_out) VALUES (?, ?, ?, ?)";
      try (PreparedStatement statement = connection.prepareStatement(sql)) {
        for (Presence presence : presences) {
          if (validator.isValid(presence)) {
            populateStatement(presence, statement);
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

  private void populateStatement(Presence presence, PreparedStatement statement) throws SQLException {
    statement.setInt(1, presence.getId());
    statement.setInt(2, presence.getPerson().getId());
    statement.setTimestamp(3, Timestamp.valueOf(presence.getTimeIn()));
    statement.setTimestamp(4, Timestamp.valueOf(presence.getTimeOut()));
  }
}
