package org.opensourcecommunity.pomodoroapp.services;

import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserSettingsNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserSettingsMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsService {

	UserSettingsRepository userSettingsRepository;
	UserSettingsMapper userSettingsMapper;

	public UserSettingsService(UserSettingsRepository userSettingsRepository,
			UserSettingsMapper userSettingsMapper) {
		this.userSettingsRepository = userSettingsRepository;
		this.userSettingsMapper = userSettingsMapper;
	}

	public UserSettings getUserSettingById(Long id) {
		return userSettingsRepository.findById(id)
				.orElseThrow(() -> new UserSettingsNotFoundException("Could not find user settings"));
	}

	public UserSettingsDto getUserSettingsByUser(User user) {
		UserSettings userSettings = user.getUserSettings();
		return userSettingsMapper.userSettingsToUserSettingsDto(userSettings);
	}

	public UserSettings createUserSettings(User user) {
		UserSettings userSettings = new UserSettings();
		userSettings.setUser(user);

		UserSettings savedUserSettings = userSettingsRepository.save(userSettings);
		user.setUserSettings(savedUserSettings);

		return savedUserSettings;
	}

	public UserSettingsDto userSettingsToUserSettingsDto(UserSettings userSettings) {
		return userSettingsMapper.userSettingsToUserSettingsDto(userSettings);
	}

	public UserSettingsDto updateUserSettings(User user, UserSettingsDto dto) {
		// UserSettings userSettings = userSettingsRepository.findByUserId(userId);
		// userSettings.setStudyTime(dto.studyTime());
		// userSettings.setLongBreakTime(dto.longBreakTime());
		// userSettings.setShortBreakTime(dto.shortBreakTime());
		//
		// UserSettings updatedUserSettings = userSettingsRepository.save(userSettings);

		// Since the Authentication strips alot of information, and we are asting it to
		// a User in the controller, this might not perform the desired operation
		// UserSettings userSettings =
		// userSettingsMapper.userSettingsDtoToUserSettings(dto, user);
		UserSettings userSettings = userSettingsRepository.findByUser(user);
		userSettings.setTaskSeconds(dto.taskSeconds());
		userSettings.setShortBreakSeconds(dto.shortBreakSeconds());
		userSettings.setLongBreakSeconds(dto.longBreakSeconds());
		userSettings.setPomodoroInterval(dto.pomodoroInterval());
		userSettings.setSound(dto.sound());
		UserSettings updatedUserSettings = userSettingsRepository.save(userSettings);

		return userSettingsMapper.userSettingsToUserSettingsDto(updatedUserSettings);
	}

}
