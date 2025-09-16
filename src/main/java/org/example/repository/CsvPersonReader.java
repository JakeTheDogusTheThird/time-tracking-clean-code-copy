package org.example.repository;

import org.example.model.Person;

public class CsvPersonReader extends AbstractCsvReader<Person> {
  private final int NAME = 1;
  private final int PHONE = 2;

  public CsvPersonReader(String csvPath) {
    super(csvPath);
  }

  protected Person mapRowToEntity(String[] row) {
    if (row == null) {
      return null;
    }
    return new Person(row[NAME], row[PHONE]);
  }
}
