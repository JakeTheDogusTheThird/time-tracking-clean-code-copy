package org.example.repository;

import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.example.model.Person;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.opencsv.CSVReader;

public class CsvPersonReader implements ReaderToEntityMapper<Person> {
  private final String personCsvPath;
  private final int ID = 0;
  private final int NAME = 1;
  private final int PHONE = 2;

  public CsvPersonReader(String personCsvPath) {
    this.personCsvPath = personCsvPath;
  }

  @Override
  public Person get(int id) {
    String[] row = getRow(id);
    return mapRowToPerson(row);
  }

  private String[] getRow(int id) {
    // might be a default implementation in the interface, or an abstract class check dependency injection
    String[] nextLine;
    try (CSVReader reader = new CSVReader(new FileReader(this.personCsvPath))) {
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine[ID].equals(String.valueOf(id))) {
          return nextLine;
        }
      }
    } catch (CsvValidationException | IOException e) {
      throw new DataAccessException(e.getMessage(), e.getCause());
    }
    return nextLine;
  }

  private List<String[]> readAllCsvFile() throws DataAccessException {
    // might be a default implementation in the interface, or an abstract class check dependency injection
    try (CSVReader reader = new CSVReader(new FileReader(this.personCsvPath))) {
      return reader.readAll();
    } catch (IOException | CsvException e) {
      throw new DataAccessException(e.getMessage(), e.getCause());
    }
  }

  private Person mapRowToPerson(String[] row) {
    // might be a part of the interface
    if (row == null) {
      return null;
    }
    Person person = new Person(row[NAME], row[PHONE]);
    person.setId(Integer.parseInt(row[ID]));
    return person;
  }

  @Override
  public List<Person> getAll() throws DataAccessException {
    List<String[]> csvRows = readAllCsvFile();
    return csvRows.stream()
        .map(this::mapRowToPerson)
        .collect(Collectors.toList());
  }
}
