package org.example.repository;

import org.example.model.Task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvTaskReader extends AbstractCsvReader<Task> {
  private static final int PROJECT = 1;
  private static final int NAME = 2;
  private static final int START = 3;
  private static final int END = 4;
  private static final int ESTIMATION = 5;
  private static final int COMPLETED = 6;

  public CsvTaskReader(String csvPath) {
    super(csvPath);
  }

  @Override
  protected Task mapRowToEntity(String[] row) {
    if (row == null) {
      return null;
    }
    return new Task(
        row[PROJECT],
        row[NAME],
        parseDateTime(row[START]),
        parseDateTime(row[END]),
        Double.parseDouble(row[ESTIMATION]),
        Boolean.parseBoolean(row[COMPLETED])
    );
  }

  private LocalDateTime parseDateTime(String time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(time, formatter);
  }
}
