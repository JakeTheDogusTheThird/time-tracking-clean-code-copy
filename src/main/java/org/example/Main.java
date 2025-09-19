package org.example;

import org.example.datasources.MySqlDataSource;
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
  private final static String PERSON_CSV = "src/main/resources/csv/person.csv";
  private final static String PRESENCE_CSV = "src/main/resources/csv/presence.csv";
  private final static String TASK_CSV = "src/main/resources/csv/task.csv";
  private final static String TIME_TRACKING_CSV = "src/main/resources/csv/timeTracking.csv";
  private final static double MARGIN_OF_ERROR = 0.001;

  public static void main(String[] args) {
    CsvPersonReader personReader = new CsvPersonReader(PERSON_CSV);
    List<Person> persons = personReader.getAll();
    Person alex = personReader.get(1);

    System.out.println(alex);
    System.out.println(persons);

    CsvPresenceReader presenceReader = new CsvPresenceReader(PRESENCE_CSV, personReader);
    List<Presence> presences = presenceReader.getAll();

    for (Presence presence : presences) {
      System.out.println(presence);
    }

    CsvTaskReader taskReader = new CsvTaskReader(TASK_CSV);
    List<Task> tasks = taskReader.getAll();

    for (Task task : tasks) {
      System.out.println(task);
    }

    CsvTimeTrackingReader timeTrackingReader = new CsvTimeTrackingReader(
        TIME_TRACKING_CSV,
        presenceReader,
        taskReader
    );

    List<TimeTracking> timeTrackings = timeTrackingReader.getAll();
    for (TimeTracking timeTracking : timeTrackings) {
      System.out.println(timeTracking);
    }

    MySqlDataSource mySqlDataSource = new MySqlDataSource();

    DBTableCleaner.deleteRowsInTables(mySqlDataSource);

    PersonValidator personValidator = new PersonValidator();
    JdbcPersonDao personDao = new JdbcPersonDao(mySqlDataSource, personValidator);
    personDao.saveAll(persons);

    PresenceValidator presenceValidator = new PresenceValidator(personValidator);
    JdbcPresenceDao presenceDao = new JdbcPresenceDao(mySqlDataSource, presenceValidator);
    presenceDao.saveAll(presences);

    TaskValidator taskValidator = new TaskValidator();
    JdbcTaskDao taskDao = new JdbcTaskDao(mySqlDataSource, taskValidator);
    taskDao.saveAll(tasks);

    TimeTrackingValidator timeTrackingValidator = new TimeTrackingValidator(
        presenceValidator,
        taskValidator
    );
    JdbcTimeTrackingDao timeTrackingDao = new JdbcTimeTrackingDao(mySqlDataSource, timeTrackingValidator);
    timeTrackingDao.saveAll(timeTrackings);

    PersonRankCalculator personRankCalculator = new PersonRankCalculator();
    GenericComparator<Person> personComparator = new GenericComparator<>(personRankCalculator, MARGIN_OF_ERROR);

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