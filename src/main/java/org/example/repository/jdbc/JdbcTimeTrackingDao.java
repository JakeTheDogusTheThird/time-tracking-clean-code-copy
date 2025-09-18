package org.example.repository.jdbc;

import org.example.datasources.OracleXeDataSource;
import org.example.model.TimeTracking;
import org.example.service.TimeTrackingValidator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcTimeTrackingDao extends AbstractJdbcDao<TimeTracking> {
  private final TimeTrackingValidator validator;

  public JdbcTimeTrackingDao(OracleXeDataSource dataSource, TimeTrackingValidator validator) {
    super(dataSource);
    this.validator = validator;
  }

  @Override
  protected String getInsertSql() {
    return """
          INSERT INTO time_trackings (id, presence_id, task_id, time_from, time_to)
          VALUES (?, ?, ?, ?, ?)
          """;
  }

  @Override
  protected boolean isValid(TimeTracking timetracking) {
    return validator.isValid(timetracking);
  }

  @Override
  protected void buildStatement(TimeTracking timeTracking, PreparedStatement statement) throws SQLException {
    statement.setInt(1, timeTracking.getId());
    statement.setInt(2, timeTracking.getPresence().getId());
    statement.setInt(3, timeTracking.getTask().getId());
    statement.setTimestamp(4, Timestamp.valueOf(timeTracking.getTimeFrom()));
    statement.setTimestamp(5, Timestamp.valueOf(timeTracking.getTimeTo()));
  }
}
