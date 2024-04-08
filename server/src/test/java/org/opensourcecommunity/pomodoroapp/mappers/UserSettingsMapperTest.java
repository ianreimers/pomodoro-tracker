package org.opensourcecommunity.pomodoroapp.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

public class UserSettingsMapperTest {
  private UserSettingsMapper mapper;
  private User user;
  private UserSettings userSettings;
  private UserSettingsDto userSettingsDto;

  @BeforeEach
  void setUp() {
    mapper = new UserSettingsMapper();
    user = TestDataFactory.createUser();
    userSettings = TestDataFactory.createUserSettings(1L, user);
    userSettingsDto = TestDataFactory.createUserSettingsDto(userSettings);
  }

  @Test
  void userSettingsToUserSettingsDto_WhenUserSettingsIsValid_ReturnUserSettingsDto() {
    UserSettingsDto responseDto = mapper.userSettingsToUserSettingsDto(userSettings);

    assertNotNull(responseDto);
    assertEquals(userSettings.getTaskSeconds(), responseDto.taskSeconds());
    assertEquals(userSettings.getShortBreakSeconds(), responseDto.shortBreakSeconds());
    assertEquals(userSettings.getLongBreakSeconds(), responseDto.longBreakSeconds());
    assertEquals(userSettings.getPomodoroInterval(), responseDto.pomodoroInterval());
    assertEquals(userSettings.getSound(), responseDto.sound());
  }

  @Test
  void userSettingsDtoToUserSettings_WhenUserAndUserSettingsDtoIsValid_ReturnUserSettings() {
    UserSettings responseUserSettings = mapper.userSettingsDtoToUserSettings(userSettingsDto, user);

    assertNotNull(responseUserSettings);
    assertEquals(userSettingsDto.taskSeconds(), responseUserSettings.getTaskSeconds());
    assertEquals(userSettingsDto.shortBreakSeconds(), responseUserSettings.getShortBreakSeconds());
    assertEquals(userSettingsDto.longBreakSeconds(), responseUserSettings.getLongBreakSeconds());
    assertEquals(userSettingsDto.pomodoroInterval(), responseUserSettings.getPomodoroInterval());
    assertEquals(userSettingsDto.sound(), responseUserSettings.getSound());
  }
}
