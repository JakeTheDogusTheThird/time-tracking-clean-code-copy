package org.example.repository;

import org.example.model.Person;
import org.example.model.Presence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CsvPresenceReader extends AbstractCsvReader<Presence> {
  private static final int PERSON_ID = 1;
  private static final int TIME_IN = 2;
  private static final int TIME_OUT = 3;

  private final CsvPersonReader personReader;

  CsvPresenceReader(String csvPath, CsvPersonReader personReader) {
    super(csvPath);
    this.personReader = personReader;
  }

  @Override
  protected Presence mapRowToEntity(String[] row) {
    if (row == null) {
      return null;
    }
    Person person = personReader.get(Integer.parseInt(row[PERSON_ID]));
    return new Presence(
        person,
        parseDateTime(row[TIME_IN]),
        parseDateTime(row[TIME_OUT])
    );
  }

  private LocalDateTime parseDateTime(String time) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(time, formatter);
  }
}
