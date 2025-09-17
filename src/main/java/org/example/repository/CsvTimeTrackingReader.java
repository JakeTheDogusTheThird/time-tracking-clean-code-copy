package org.example.repository;

import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;

public class CsvTimeTrackingReader extends AbstractCsvReader<TimeTracking>{
  private final CsvPresenceReader presenceReader;
  private final CsvTaskReader taskReader;

  private static final int PRESENCE_ID = 1;
  private static final int TASK_ID = 2;
  private static final int FROM = 3;
  private static final int TO = 4;

  public CsvTimeTrackingReader(
      String csvPath,
      CsvPresenceReader presenceReader,
      CsvTaskReader taskReader
  ) {
    super(csvPath);
    this.presenceReader = presenceReader;
    this.taskReader = taskReader;
  }

  @Override
  protected TimeTracking mapRowToEntity(String[] row) {
    if (row == null) {
      return null;
    }
    Presence presence = presenceReader.get(Integer.parseInt(row[PRESENCE_ID]));
    Task task = taskReader.get(Integer.parseInt(row[TASK_ID]));

    TimeTracking timeTracking = new TimeTracking(
        presence,
        task,
        parseDateTime(row[FROM]),
        parseDateTime(row[TO])
    );
    timeTracking.setId(Integer.parseInt(row[ID]));
    return timeTracking;
  }
}
