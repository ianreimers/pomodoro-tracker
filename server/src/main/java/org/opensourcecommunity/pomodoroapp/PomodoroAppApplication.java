package org.opensourcecommunity.pomodoroapp;

import com.github.javafaker.Faker;
import java.time.DayOfWeek;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.UUID;
import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.PomodoroSessionRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PomodoroAppApplication {

  public static void main(String[] args) {
    SpringApplication.run(PomodoroAppApplication.class, args);
  }

  // @Bean
  public CommandLineRunner commandLineRunner(
      UserRepository userRepository,
      UserSettingsRepository userSettingsRepository,
      PomodoroSessionRepository pomodoroSessionRepository) {

    return args -> {
      Faker faker = new Faker();
      // Create a user
      // User user = User.builder()
      // .username("thedude")
      // .email(faker.internet().emailAddress())
      // .password("password")
      // .build();
      // User responseUser = userRepository.save(user);

      User responseUser = userRepository.findByUsername("thedale").get();
      UserSettings userSettings = userSettingsRepository.findByUserId(responseUser.getId()).get();

      // Add the users default settings
      // UserSettings userSettings = UserSettings.builder()
      // .user(responseUser)
      // .build();
      // userSettingsRepository.save(userSettings);

      Boolean weekIterated = false;
      // Monday = 1, Sunday = 7
      for (DayOfWeek day = DayOfWeek.MONDAY;
          (!weekIterated && day.getValue() <= DayOfWeek.SUNDAY.getValue());
          day = day.plus(1)) {
        ZonedDateTime lastMonday =
            ZonedDateTime.now().with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        ZonedDateTime specificDay = lastMonday.with(day);

        // The start of each day
        ZonedDateTime sessionStartTime = specificDay.withHour(9); // 9 AM
        ZonedDateTime sessionUpdateTime = specificDay.withHour(17); // 5 PM
        if (day.getValue() == DayOfWeek.SUNDAY.getValue()) {
          weekIterated = true;
        }

        // Create a pomodoro session that a user "completed"
        PomodoroSession pomodoroSession =
            PomodoroSession.builder()
                .user(responseUser)
                .tempUuid(UUID.randomUUID())
                .sessionTaskSeconds(userSettings.getTaskSeconds())
                .sessionLongBreakSeconds(userSettings.getLongBreakSeconds())
                .sessionShortBreakSeconds(userSettings.getShortBreakSeconds())
                .sessionStartTime(sessionStartTime)
                .sessionUpdateTime(sessionUpdateTime)
                .breakDuration(
                    day.getValue() % 2 == 0
                        ? faker.number().numberBetween(0, userSettings.getShortBreakSeconds())
                        : userSettings.getShortBreakSeconds())
                .taskDuration(
                    day.getValue() % 2 == 0
                        ? faker.number().numberBetween(0, userSettings.getTaskSeconds())
                        : userSettings.getTaskSeconds())
                .breakType(day.getValue() % 2 == 0 ? BreakTypeEnum.SHORT : BreakTypeEnum.LONG)
                .build();
        pomodoroSessionRepository.save(pomodoroSession);
      }

      // for (int i = 0; i < 50; i++) {
      // // Create a user
      // User user = User.builder()
      // .username(faker.name().username())
      // .email(faker.internet().emailAddress())
      // .password("password")
      // .build();
      // User responseUser = userRepository.save(user);
      //
      // // Add the users default settings
      // UserSettings userSettings = UserSettings.builder()
      // .user(responseUser)
      // .build();
      // userSettingsRepository.save(userSettings);
      //
      // // Create a pomodoro session that a user "completed"
      // PomodoroSession pomodoroSession = PomodoroSession.builder()
      // .user(responseUser)
      // .tempUuid(UUID.randomUUID())
      // .sessionTaskSeconds(userSettings.getTaskSeconds())
      // .sessionLongBreakSeconds(userSettings.getLongBreakSeconds())
      // .sessionShortBreakSeconds(userSettings.getShortBreakSeconds())
      // .sessionStartTime(ZonedDateTime.now())
      // .sessionUpdateTime(ZonedDateTime.now())
      // .breakDuration(faker.number().numberBetween(0,
      // userSettings.getShortBreakSeconds()))
      // .taskDuration(faker.number().numberBetween(0,
      // userSettings.getTaskSeconds()))
      // .breakType(BreakTypeEnum.SHORT)
      // .build();
      // pomodoroSessionRepository.save(pomodoroSession);

      // }

    };
  }
}
