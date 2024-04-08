package org.opensourcecommunity.pomodoroapp.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

public class PomodoroSessionMapperTest {
  private PomodoroSessionMapper mapper;

  private PomodoroSession pomodoroSession;
  private PomodoroSessionResponseDto pomodoroSessionResponseDto;
  private PomodoroSessionDto pomodoroSessionDto;
  private User user;

  @BeforeEach
  void setUp() {
    mapper = new PomodoroSessionMapper();
    user = TestDataFactory.createUser();
    pomodoroSession = TestDataFactory.createPomodoroSession();
    pomodoroSessionDto = TestDataFactory.createPomodoroSessionDto(pomodoroSession);
    pomodoroSessionResponseDto = TestDataFactory.createPomodoroSessionResponseDto(pomodoroSession);
  }

  @Test
  void pomodoroSessionDtoToPomodoroSession_WhenUserAndDtoIsValid_ReturnPomodoroSession() {

    PomodoroSession responsePomodoroSession =
        mapper.pomodoroSessionDtoToPomodoroSession(user, pomodoroSessionDto);

    assertNotNull(responsePomodoroSession);
    assertEquals(pomodoroSession.getTempUuid(), responsePomodoroSession.getTempUuid());
    assertEquals(pomodoroSession.getTaskDuration(), responsePomodoroSession.getTaskDuration());
    assertEquals(pomodoroSession.getBreakDuration(), responsePomodoroSession.getBreakDuration());
    assertEquals(
        pomodoroSession.getSessionTaskSeconds(), responsePomodoroSession.getSessionTaskSeconds());
    assertEquals(
        pomodoroSession.getSessionShortBreakSeconds(),
        responsePomodoroSession.getSessionShortBreakSeconds());
    assertEquals(
        pomodoroSession.getSessionLongBreakSeconds(),
        responsePomodoroSession.getSessionLongBreakSeconds());
    assertEquals(
        pomodoroSession.getSessionStartTime(), responsePomodoroSession.getSessionStartTime());
    assertEquals(
        pomodoroSession.getSessionUpdateTime(), responsePomodoroSession.getSessionUpdateTime());
    assertEquals(pomodoroSession.getBreakType(), responsePomodoroSession.getBreakType());
  }

  @Test
  void pomodoroSessionToPomodoroResponseDto_WhenPomodoroIsValid_ReturnResponseDto() {
    PomodoroSessionResponseDto responseDto =
        mapper.pomodoroSessionToPomodoroResponseDto(pomodoroSession);

    assertNotNull(responseDto);
    assertEquals(pomodoroSession.getId(), responseDto.id());
    assertEquals(pomodoroSession.getTempUuid(), responseDto.tempUuid());
    assertEquals(pomodoroSession.getTaskDuration(), responseDto.taskDuration());
    assertEquals(pomodoroSession.getBreakDuration(), responseDto.breakDuration());
    assertEquals(pomodoroSession.getSessionTaskSeconds(), responseDto.sessionTaskSeconds());
    assertEquals(
        pomodoroSession.getSessionShortBreakSeconds(), responseDto.sessionShortBreakSeconds());
    assertEquals(
        pomodoroSession.getSessionLongBreakSeconds(), responseDto.sessionLongBreakSeconds());
    assertEquals(pomodoroSession.getSessionStartTime(), responseDto.sessionStartTime());
    assertEquals(pomodoroSession.getSessionUpdateTime(), responseDto.sessionUpdateTime());
    assertEquals(pomodoroSession.getBreakType(), responseDto.breakType());
  }

  @Test
  void pomodoroSessionsToPomodoroResponseDtos_WhenPomodorosAreValid_ReturnResponseDtos() {
    List<PomodoroSession> pomodoroSessions = Arrays.asList(pomodoroSession);

    List<PomodoroSessionResponseDto> responseDtos =
        mapper.pomodoroSessionsToPomodoroResponseDtos(pomodoroSessions);

    assertNotNull(responseDtos);
    assertEquals(1, responseDtos.size());
  }
}
