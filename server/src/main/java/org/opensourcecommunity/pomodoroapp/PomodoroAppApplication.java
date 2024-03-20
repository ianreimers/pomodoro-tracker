package org.opensourcecommunity.pomodoroapp;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
import org.springframework.context.annotation.Bean;

import com.github.javafaker.Faker;

@SpringBootApplication
public class PomodoroAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PomodoroAppApplication.class, args);
	}

	// @Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository,
			UserSettingsRepository userSettingsRepository,
			PomodoroSessionRepository pomodoroSessionRepository) {

		return args -> {

			Faker faker = new Faker();

			for (int i = 0; i < 50; i++) {
				// Create a user
				User user = User.builder()
						.username(faker.name().username())
						.email(faker.internet().emailAddress())
						.password(faker.internet().password(8, 12))
						.build();
				User responseUser = userRepository.save(user);

				// Add the users default settings
				UserSettings userSettings = UserSettings.builder()
						.user(responseUser)
						.build();
				userSettingsRepository.save(userSettings);

				// Create a pomodoro session that a user "completed"
				PomodoroSession pomodoroSession = PomodoroSession.builder()
						.user(responseUser)
						.tempUuid(UUID.randomUUID())
						.sessionStudyTime(userSettings.getTaskSeconds())
						.sessionLongBreakTime(userSettings.getLongBreakSeconds())
						.sessionShortBreakTime(userSettings.getShortBreakSeconds())
						.sessionStartTime(ZonedDateTime.now())
						.sessionUpdateTime(ZonedDateTime.now())
						.breakDuration(faker.number().numberBetween(0,
								userSettings.getShortBreakSeconds()))
						.studyDuration(faker.number().numberBetween(0,
								userSettings.getTaskSeconds()))
						.breakType(BreakTypeEnum.SHORT)
						.build();
				pomodoroSessionRepository.save(pomodoroSession);

			}

		};
	}

}
