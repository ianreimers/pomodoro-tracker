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

  public UserSettingsService(
      UserSettingsRepository userSettingsRepository, UserSettingsMapper userSettingsMapper) {
    this.userSettingsRepository = userSettingsRepository;
    this.userSettingsMapper = userSettingsMapper;
  }

  public UserSettings getUserSettingById(Long id) {
    if (id == null) {
      throw new IllegalArgumentException("User settings id should not be null");
    }

    return userSettingsRepository
        .findById(id)
        .orElseThrow(() -> new UserSettingsNotFoundException("Could not find user settings"));
  }

  public UserSettingsDto getUserSettingsByUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User should not be null");
    }

    UserSettings userSettings = user.getUserSettings();

    if (userSettings == null) {
      throw new UserSettingsNotFoundException(
          "Could not find user settings for user " + user.getUsername());
    }

    return userSettingsMapper.userSettingsToUserSettingsDto(userSettings);
  }

  public UserSettings createUserSettings(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User should not be null");
    }

    UserSettings userSettings = new UserSettings();
    userSettings.setUser(user);

    UserSettings savedUserSettings = userSettingsRepository.save(userSettings);
    user.setUserSettings(savedUserSettings);

    return savedUserSettings;
  }

  public UserSettingsDto userSettingsToUserSettingsDto(UserSettings userSettings) {
    if (userSettings == null) {
      throw new IllegalArgumentException("User settings should not be null");
    }

    return userSettingsMapper.userSettingsToUserSettingsDto(userSettings);
  }

  public UserSettingsDto updateUserSettings(User user, UserSettingsDto dto) {
    if (user == null || dto == null) {
      throw new IllegalArgumentException("User and user settings dto should not be null");
    }

    UserSettings userSettings =
        userSettingsRepository
            .findByUser(user)
            .orElseThrow(
                () ->
                    new UserSettingsNotFoundException(
                        "Could not find user settings for user " + user.getUsername()));
    userSettings.setTaskSeconds(dto.taskSeconds());
    userSettings.setShortBreakSeconds(dto.shortBreakSeconds());
    userSettings.setLongBreakSeconds(dto.longBreakSeconds());
    userSettings.setPomodoroInterval(dto.pomodoroInterval());
    userSettings.setSound(dto.sound());
    UserSettings updatedUserSettings = userSettingsRepository.save(userSettings);

    return userSettingsMapper.userSettingsToUserSettingsDto(updatedUserSettings);
  }
}
