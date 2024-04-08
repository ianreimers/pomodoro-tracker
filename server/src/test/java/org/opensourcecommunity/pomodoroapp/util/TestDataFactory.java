package org.opensourcecommunity.pomodoroapp.util;

import java.time.ZonedDateTime;
import java.util.UUID;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionPatchDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.Role;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;

public class TestDataFactory {

  public static User createUser() {
    return createUser(1L);
  }

  public static User createUser(Long userId) {
    return User.builder()
        .id(userId)
        .username("user")
        .email("user@example.com")
        .password("password")
        .role(Role.USER)
        .build();
  }

  public static UserDto createUserDto() {
    User user = createUser();
    return new UserDto(user.getUsername(), user.getEmail(), user.getPassword());
  }

  public static UserDto createUserDto(User user) {
    return new UserDto(user.getUsername(), user.getEmail(), user.getPassword());
  }

  public static UserResponseDto createUserResponseDto() {
    User user = createUser();
    UserSettings userSettings = createUserSettings(1L, user);
    return new UserResponseDto(1L, "user", "user@example.com", userSettings);
  }

  public static UserResponseDto createUserResponseDto(User user) {
    return new UserResponseDto(
        user.getId(), user.getUsername(), user.getEmail(), user.getUserSettings());
  }

  public static UserResponseDto createUserResponseDto(User user, UserSettings userSettings) {
    return new UserResponseDto(1L, "user", "user@example.com", userSettings);
  }

  public static UserSettings createUserSettings(Long userSettingsId, User user) {
    return UserSettings.builder()
        .id(userSettingsId)
        .taskSeconds(1500)
        .shortBreakSeconds(600)
        .longBreakSeconds(1800)
        .pomodoroInterval(4)
        .sound("bells")
        .user(user)
        .build();
  }

  public static UserSettingsDto createUserSettingsDto(UserSettings userSettings) {
    return new UserSettingsDto(
        userSettings.getTaskSeconds(),
        userSettings.getShortBreakSeconds(),
        userSettings.getLongBreakSeconds(),
        userSettings.getPomodoroInterval(),
        userSettings.getSound());
  }

  public static PomodoroSession createPomodoroSession() {
    return PomodoroSession.builder()
        .id(1L)
        .tempUuid(UUID.randomUUID())
        .taskDuration(0)
        .sessionTaskSeconds(1500)
        .sessionShortBreakSeconds(600)
        .sessionLongBreakSeconds(1900)
        .breakDuration(0)
        .breakType(BreakTypeEnum.SHORT)
        .build();
  }

  public static PomodoroSession createPomodoroSession(User user) {
    return PomodoroSession.builder()
        .id(1L)
        .tempUuid(UUID.randomUUID())
        .taskDuration(0)
        .sessionTaskSeconds(1500)
        .sessionStartTime(ZonedDateTime.now())
        .sessionUpdateTime(ZonedDateTime.now())
        .sessionShortBreakSeconds(600)
        .sessionLongBreakSeconds(1900)
        .breakDuration(0)
        .breakType(BreakTypeEnum.SHORT)
        .user(user)
        .build();
  }

  public static PomodoroSessionPatchDto createPomodoroSessionPatchDto(String sessionType) {
    return new PomodoroSessionPatchDto(sessionType);
  }

  public static PomodoroSessionResponseDto createPomodoroSessionResponseDto(
      PomodoroSession pomodoro) {
    return new PomodoroSessionResponseDto(
        pomodoro.getId(),
        pomodoro.getTempUuid(),
        pomodoro.getTaskDuration(),
        pomodoro.getBreakDuration(),
        pomodoro.getSessionTaskSeconds(),
        pomodoro.getSessionShortBreakSeconds(),
        pomodoro.getSessionLongBreakSeconds(),
        pomodoro.getSessionStartTime(),
        pomodoro.getSessionUpdateTime(),
        pomodoro.getBreakType());
  }

  public static PomodoroSessionDto createPomodoroSessionDto(PomodoroSession pomodoro) {
    return new PomodoroSessionDto(
        pomodoro.getTempUuid(),
        pomodoro.getTaskDuration(),
        pomodoro.getBreakDuration(),
        pomodoro.getSessionTaskSeconds(),
        pomodoro.getSessionShortBreakSeconds(),
        pomodoro.getSessionLongBreakSeconds(),
        pomodoro.getSessionStartTime(),
        pomodoro.getSessionUpdateTime(),
        pomodoro.getBreakType());
  }
}
