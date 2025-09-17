package org.example.repository.jdbc;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Person;
import org.example.service.PersonValidator;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JdbcPersonDao extends AbstractJdbcDao<Person> {
  private final PersonValidator validator;

  public JdbcPersonDao(OracleXeDataSource dataSource, PersonValidator validator) {
    super(dataSource);
    this.validator = validator;
  }

  @Override
  protected String getInsertSql() {
    return "INSERT INTO persons (id, name, phone) VALUES (?, ?, ?)";
  }

  @Override
  protected void buildStatement(Person person, PreparedStatement ps) throws SQLException {
    ps.setInt(1, person.getId());
    ps.setString(2, person.getName());
    ps.setString(3, person.getPhone());
  }

  @Override
  protected boolean isValid(Person entity) {
    return validator.isValid(entity);
  }
}
