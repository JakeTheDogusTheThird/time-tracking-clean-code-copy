package org.example.utility;

import org.example.datasources.OracleXeDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTableCleaner {
  public static void deleteRowsInTables(OracleXeDataSource dataSource) {
    try (Connection connection = dataSource.getConnection()) {
      connection.setAutoCommit(false); // Start transaction

      try (Statement statement = connection.createStatement()) {
        statement.addBatch("DELETE FROM time_trackings");
        statement.addBatch("DELETE FROM tasks"); // Example with foreign key
        statement.addBatch("DELETE FROM presences");
        statement.addBatch("DELETE FROM persons");

        statement.executeBatch();

        connection.commit(); // Commit the transaction
        System.out.println("Multiple tables deleted successfully within a transaction.");

      } catch (SQLException e) {
        connection.rollback(); // Rollback on error
        System.err.println("Transaction failed, rolling back: " + e.getMessage());
      }

    } catch (SQLException e) {
      System.err.println("Database connection error: " + e.getMessage());
    }
  }
}
