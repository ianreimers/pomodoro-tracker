package org.opensourcecommunity.pomodoroapp;

import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PomodoroAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(PomodoroAppApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(UserRepository userRepository,
			UserSettingsRepository userSettingsRepository) {

		return args -> {
			var user = User.builder()
					.username("thedale")
					.email("thedale@gmail.com")
					.password("dateiscool123")
					.build();
			var userSettings = UserSettings.builder()
					.build();

			userRepository.save(user);
			userSettingsRepository.save(userSettings);

		};
	}

}
