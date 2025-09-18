package org.example;

import org.example.datasources.OracleXeDataSource;
import org.example.model.Person;
import org.example.model.Presence;
import org.example.model.Task;
import org.example.model.TimeTracking;
import org.example.repository.csv.CsvPersonReader;
import org.example.repository.csv.CsvPresenceReader;
import org.example.repository.csv.CsvTaskReader;
import org.example.repository.csv.CsvTimeTrackingReader;
import org.example.repository.jdbc.JdbcPersonDao;
import org.example.repository.jdbc.JdbcPresenceDao;
import org.example.repository.jdbc.JdbcTaskDao;
import org.example.repository.jdbc.JdbcTimeTrackingDao;
import org.example.service.*;
import org.example.utility.DBTableCleaner;

import java.util.ArrayList;
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

      DBTableCleaner.deleteRowsInTables(oracleXeDataSource);

      PersonValidator personValidator = new PersonValidator();
      JdbcPersonDao personDao = new JdbcPersonDao(oracleXeDataSource, personValidator);
      personDao.saveAll(persons);

      PresenceValidator presenceValidator = new PresenceValidator(personValidator);
      JdbcPresenceDao presenceDao = new JdbcPresenceDao(oracleXeDataSource, presenceValidator);
      presenceDao.saveAll(presences);

      TaskValidator taskValidator = new TaskValidator();
      JdbcTaskDao taskDao = new JdbcTaskDao(oracleXeDataSource, taskValidator);
      taskDao.saveAll(tasks);

      TimeTrackingValidator timeTrackingValidator = new TimeTrackingValidator(
          presenceValidator,
          taskValidator
      );
      JdbcTimeTrackingDao timeTrackingDao = new JdbcTimeTrackingDao(oracleXeDataSource, timeTrackingValidator);
      timeTrackingDao.saveAll(timeTrackings);

      PersonRankCalculator  personRankCalculator = new PersonRankCalculator();
      GenericComparator<Person> personComparator = new GenericComparator<>(personRankCalculator);

      List<Person> temp = new ArrayList<>(persons);

      temp.sort(personComparator);
      System.out.println();
      for (Person person : persons) {
        System.out.println(person);
      }

      for (Person person : temp) {
        System.out.println(person);
      }
    }
}