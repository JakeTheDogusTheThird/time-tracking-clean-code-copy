package org.example.repository.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.example.repository.exceptions.DataAccessException;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractCsvReader<T> implements EntityReader<T> {
  protected final String csvPath;
  protected final int ID = 0;

  public AbstractCsvReader(String csvPath) {
    this.csvPath = csvPath;
  }

  @Override
  public T get(int id) {
    String[] row = getRow(id);
    return mapRowToEntity(row);
  }

  @Override
  public List<T> getAll() {
    List<String[]> rows = readAllLines();
    return rows.stream()
        .map(this::mapRowToEntity)
        .toList();
  }

  protected String[] getRow(int id) {
    try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        if (nextLine[ID].equals(String.valueOf(id))) {
          return nextLine;
        }
      }
    } catch (IOException | CsvValidationException e) {
      throw new DataAccessException(e.getMessage(), e);
    }
    return null;
  }

  protected List<String[]> readAllLines() {
    try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
      return reader.readAll();
    } catch (IOException | CsvException e) {
      throw new DataAccessException(e.getMessage(), e);
    }
  }

  protected LocalDateTime parseDateTime(String time) {
    if (time.isBlank()) {
      return null;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    return LocalDateTime.parse(time, formatter);
  }

  protected abstract T mapRowToEntity(String[] row);
}
