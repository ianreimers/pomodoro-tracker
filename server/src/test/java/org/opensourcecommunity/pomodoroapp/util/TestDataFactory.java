package org.opensourcecommunity.pomodoroapp.util;

import java.util.UUID;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.Role;
import org.opensourcecommunity.pomodoroapp.models.User;

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
        .sessionShortBreakSeconds(600)
        .sessionLongBreakSeconds(1900)
        .breakDuration(0)
        .breakType(BreakTypeEnum.SHORT)
        .user(user)
        .build();
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
