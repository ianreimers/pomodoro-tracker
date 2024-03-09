package org.opensourcecommunity.pomodoroapp.mappers;

import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.springframework.stereotype.Service;

@Service
public class UserSettingsMapper {

	public UserSettingsDto userSettingsToUserSettingsDto(UserSettings userSettings) {
		return new UserSettingsDto(
				userSettings.getStudyTime(),
				userSettings.getShortBreakTime(),
				userSettings.getLongBreakTime());

	}

}
