package org.example.repository.jdbc;

import org.example.repository.exceptions.DataAccessException;
import org.example.datasources.OracleXeDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public abstract class AbstractJdbcDao<T> implements Dao<T> {
  protected final OracleXeDataSource dataSource;

  protected AbstractJdbcDao(OracleXeDataSource dataSource) {
    this.dataSource = dataSource;
  }

  @Override
  public void saveAll(List<T> entities) throws DataAccessException {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false);
      try (PreparedStatement statement = connection.prepareStatement(getInsertSql())) {
        for (T entity : entities) {
          if (isValid(entity)) {
            buildStatement(entity, statement);
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

  protected abstract String getInsertSql();
  protected abstract void buildStatement(T entity, PreparedStatement ps) throws SQLException;
  protected abstract boolean isValid(T entity);
}
