package org.opensourcecommunity.pomodoroapp.mappers;

import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsMapper {

	public UserSettingsDto userSettingsToUserSettingsDto(UserSettings userSettings) {
		return new UserSettingsDto(
				userSettings.getTaskSeconds(),
				userSettings.getShortBreakSeconds(),
				userSettings.getLongBreakSeconds(),
				userSettings.getPomodoroInterval(),
				userSettings.getSound());
	}

	public UserSettings userSettingsDtoToUserSettings(UserSettingsDto dto, User user) {
		return UserSettings
				.builder()
				.taskSeconds(dto.taskSeconds())
				.shortBreakSeconds(dto.shortBreakSeconds())
				.longBreakSeconds(dto.longBreakSeconds())
				.pomodoroInterval(dto.pomodoroInterval())
				.sound(dto.sound())
				.user(user)
				.build();
	}

}
