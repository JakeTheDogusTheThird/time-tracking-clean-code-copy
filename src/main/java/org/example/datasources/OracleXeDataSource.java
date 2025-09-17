package org.example.datasources;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class OracleXeDataSource {
  private final MysqlDataSource oracleDataSource;

  public OracleXeDataSource() {
    this.oracleDataSource = new MysqlDataSource();
    this.oracleDataSource.setURL("jdbc:mysql://localhost:3306/testdb");
    this.oracleDataSource.setUser("root");
    this.oracleDataSource.setPassword("rootPass");
  }

  public Connection getConnection() throws SQLException {
    return this.oracleDataSource.getConnection();
  }
}
