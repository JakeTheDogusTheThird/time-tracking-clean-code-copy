package org.example.repository.csv;

import org.example.model.Task;

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
    Task task = new Task(
        row[PROJECT],
        row[NAME],
        parseDateTime(row[START]),
        parseDateTime(row[END]),
        Double.parseDouble(row[ESTIMATION]),
        Boolean.parseBoolean(row[COMPLETED])
    );
    task.setId(Integer.parseInt(row[ID]));
    return task;
  }
}
