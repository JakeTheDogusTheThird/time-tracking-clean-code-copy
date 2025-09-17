package org.example;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Person;
import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;
import org.example.repository.*;
import org.example.service.PersonValidator;
import org.example.service.PresenceValidator;

import java.util.List;

public class Main {
    public static void main(String[] args) {
      CsvPersonReader personReader = new CsvPersonReader("csvDataSource/person.csv");
      List<Person> persons = personReader.getAll();
      Person alex = personReader.get(1);

      System.out.println(alex);
      System.out.println(persons);

      CsvPresenceReader presenceReader = new CsvPresenceReader("csvDataSource/presence.csv", personReader);
      List<Presence> presences = presenceReader.getAll();

      for (Presence presence : presences) {
        System.out.println(presence);
      }

      CsvTaskReader taskReader = new CsvTaskReader("csvDataSource/task.csv");
      List<Task> tasks = taskReader.getAll();

      for (Task task : tasks) {
        System.out.println(task);
      }

      CsvTimeTrackingReader timeTrackingReader = new CsvTimeTrackingReader(
          "csvDataSource/timetracking.csv",
          presenceReader,
          taskReader
      );

      List<TimeTracking> timeTrackings = timeTrackingReader.getAll();

      for (TimeTracking timeTracking : timeTrackings) {
        System.out.println(timeTracking);
      }

      OracleXeDataSource oracleXeDataSource = new OracleXeDataSource();

      PersonValidator personValidator = new PersonValidator();
      JdbcPersonDao personDao = new JdbcPersonDao(oracleXeDataSource, personValidator);
      personDao.saveAll(persons);

      PresenceValidator presenceValidator = new PresenceValidator(personValidator);
      JdbcPresenceDao presenceDao = new JdbcPresenceDao(oracleXeDataSource, presenceValidator);
      presenceDao.saveAll(presences);
    }
}