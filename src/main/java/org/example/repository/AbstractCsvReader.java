package org.example.repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
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
    List<String[]> rows = readAllCsvFile();
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

  protected List<String[]> readAllCsvFile() {
    try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
      return reader.readAll();
    } catch (IOException | CsvException e) {
      throw new DataAccessException(e.getMessage(), e);
    }
  }

  protected abstract T mapRowToEntity(String[] row);
}
