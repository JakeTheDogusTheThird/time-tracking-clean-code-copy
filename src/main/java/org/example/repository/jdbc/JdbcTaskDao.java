package org.example.repository.jdbc;

import org.example.datasources.MySqlDataSource;
import org.example.model.Task;
import org.example.service.TaskValidator;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class JdbcTaskDao extends AbstractJdbcDao<Task> {
  private final TaskValidator validator;

  public JdbcTaskDao(MySqlDataSource dataSource, TaskValidator validator) {
    super(dataSource);
    this.validator = validator;
  }

  @Override
  protected String getInsertSql() {
    return """
          INSERT INTO tasks (id, project, name, start, end, estimation_in_hours, completed)
          VALUES (?, ?, ?, ?, ?, ?, ?)
          """;
  }

  @Override
  protected boolean isValid(Task task) {
    return validator.isValid(task);
  }

  @Override
  protected void buildStatement(Task task, PreparedStatement statement) throws SQLException {
    statement.setInt(1, task.getId());
    statement.setString(2, task.getProject());
    statement.setString(3, task.getName());
    statement.setTimestamp(4, Timestamp.valueOf(task.getStart()));
    statement.setTimestamp(5, Timestamp.valueOf(task.getEnd()));
    statement.setDouble(6, task.getEstimationInHours());
    statement.setBoolean(7, task.isCompleted());
  }
}
