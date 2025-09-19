package org.example.repository.jdbc;

import org.example.datasources.MySqlDataSource;
import org.example.model.Presence;
import org.example.service.PresenceValidator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcPresenceDao extends AbstractJdbcDao<Presence> {
  private final PresenceValidator validator;

  public JdbcPresenceDao(MySqlDataSource dataSource, PresenceValidator validator) {
    super(dataSource);
    this.validator = validator;
  }

  @Override
  protected String getInsertSql() {
    return "INSERT INTO presences (id, person_id, time_in, time_out) VALUES (?, ?, ?, ?)";
  }

  @Override
  protected boolean isValid(Presence presence) {
    return validator.isValid(presence);
  }

  @Override
  protected void buildStatement(Presence presence, PreparedStatement statement) throws SQLException {
    statement.setInt(1, presence.getId());
    statement.setInt(2, presence.getPerson().getId());
    statement.setTimestamp(3, Timestamp.valueOf(presence.getTimeIn()));
    statement.setTimestamp(4, Timestamp.valueOf(presence.getTimeOut()));
  }
}
