package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.mappers.PomodoroSessionMapper;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.PomodoroSessionRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class PomodoroSessionServiceTest {

  @Mock private PomodoroSessionRepository pomodoroSessionRepository;
  @Mock private PomodoroSessionMapper pomodoroSessionMapper;
  @Mock private UserRepository userRepository;

  @InjectMocks private PomodoroSessionService pomodoroSessionService;

  private User user;
  private Long userId;
  private Long pomodoroSessionId;
  private PomodoroSession pomodoroSession;

  @BeforeEach
  void setUp() {
    userId = 1L;
    pomodoroSessionId = 5L;
    user = TestDataFactory.createUser(userId);
    pomodoroSession = TestDataFactory.createPomodoroSession(user);
  }

  @Test
  void getPomodoroSessionByUser_WithValidUsername_ReturnPomodoroSessionResponseDtos() {
    // Arrange
    List<PomodoroSession> pomodoros = Arrays.asList(pomodoroSession);
    PomodoroSessionResponseDto pomodoroSessionResponseDto =
        TestDataFactory.createPomodoroSessionResponseDto(pomodoroSession);
    List<PomodoroSessionResponseDto> pomodoroSessionResponseDtos =
        Arrays.asList(pomodoroSessionResponseDto);

    when(pomodoroSessionRepository.findAllByUser(user)).thenReturn(pomodoros);
    when(pomodoroSessionMapper.pomodoroSessionsToPomodoroResponseDtos(pomodoros))
        .thenReturn(pomodoroSessionResponseDtos);

    // Act
    List<PomodoroSessionResponseDto> responsePomodoros =
        pomodoroSessionService.getPomodoroSessionsByUser(user);

    // Assert
    assertNotNull(responsePomodoros);
    assertEquals(1, responsePomodoros.size());
    verify(pomodoroSessionRepository).findAllByUser(user);
    verify(pomodoroSessionMapper).pomodoroSessionsToPomodoroResponseDtos(pomodoros);
  }

  @Test
  void getPomodoroSessionByUser_WithNullUsername_ThrowsIllegalArgumentException() {
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.getPomodoroSessionsByUser(null));

    assertEquals("User should not be null", e.getMessage());
  }

  @Test
  void createPomodoroSession_WithValidUserAndPomodoroSessionDto_ReturnPomodoroSessionResponseDto() {
    // Arrange
    PomodoroSessionDto dto = TestDataFactory.createPomodoroSessionDto(pomodoroSession);

    when(pomodoroSessionMapper.pomodoroSessionDtoToPomodoroSession(user, dto))
        .thenReturn(pomodoroSession);
    when(pomodoroSessionRepository.save(any(PomodoroSession.class))).thenReturn(pomodoroSession);
    when(pomodoroSessionMapper.pomodoroSessionToPomodoroResponseDto(any(PomodoroSession.class)))
        .thenReturn(TestDataFactory.createPomodoroSessionResponseDto(pomodoroSession));

    // Act
    PomodoroSessionResponseDto responseDto =
        pomodoroSessionService.createPomodoroSession(user, dto);

    // Assert
    assertNotNull(responseDto);
    assertEquals(pomodoroSession.getTempUuid(), responseDto.tempUuid());
    assertEquals(pomodoroSession.getId(), responseDto.id());
    assertEquals(pomodoroSession.getTaskDuration(), responseDto.taskDuration());
    assertEquals(pomodoroSession.getBreakDuration(), responseDto.breakDuration());
    assertEquals(
        pomodoroSession.getSessionShortBreakSeconds(), responseDto.sessionShortBreakSeconds());
    assertEquals(
        pomodoroSession.getSessionLongBreakSeconds(), responseDto.sessionLongBreakSeconds());
    assertEquals(pomodoroSession.getSessionStartTime(), responseDto.sessionStartTime());
    assertEquals(pomodoroSession.getSessionUpdateTime(), responseDto.sessionUpdateTime());
    assertEquals(pomodoroSession.getBreakType(), responseDto.breakType());
    verify(pomodoroSessionMapper).pomodoroSessionDtoToPomodoroSession(user, dto);
    verify(pomodoroSessionRepository).save(pomodoroSession);
  }

  @Test
  void createPomodoroSession_WithInvalidUserOrPomodoroSessionDto_ThrowIllegalArgumentsException() {
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.createPomodoroSession(null, null));
    assertEquals("User or pomodoro session dto should not be null", e.getMessage());

    e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.createPomodoroSession(user, null));
    assertEquals("User or pomodoro session dto should not be null", e.getMessage());
  }

  @Test
  void updatePomodoroSession_WithValidIdAndTaskType_UpdatesTaskDuration() {
    String sessionType = "task";

    when(pomodoroSessionRepository.findById(pomodoroSessionId))
        .thenReturn(Optional.of(pomodoroSession));

    pomodoroSessionService.updatePomodoroSession(pomodoroSessionId, sessionType);

    assertEquals(60, pomodoroSession.getTaskDuration());
    assertEquals(0, pomodoroSession.getBreakDuration());
  }

  @Test
  void updatePomodoroSession_WithInvalidIdOrBreakType_ThrowsIllegalArgumentException() {
    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.updatePomodoroSession(null, null));
    assertEquals("Pomdoro id and session type should not be null", e.getMessage());

    e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.updatePomodoroSession(1L, null));
    assertEquals("Pomdoro id and session type should not be null", e.getMessage());
  }

  @Test
  void updatePomodoroSession_WithValidIdAndBreakType_UpdatesBreakDuration() {
    String sessionType = "break";

    when(pomodoroSessionRepository.findById(pomodoroSessionId))
        .thenReturn(Optional.of(pomodoroSession));

    pomodoroSessionService.updatePomodoroSession(pomodoroSessionId, sessionType);

    assertEquals(60, pomodoroSession.getBreakDuration());
    assertEquals(0, pomodoroSession.getTaskDuration());
  }

  @Test
  void updatePomodoroSession_WithInvalidId_ThrowsEntityNotFoundException() {
    String sessionType = "task";

    EntityNotFoundException e =
        assertThrows(
            EntityNotFoundException.class,
            () -> pomodoroSessionService.updatePomodoroSession(pomodoroSessionId, sessionType));

    assertEquals("Pomodoro not found with id " + pomodoroSessionId, e.getMessage());
  }

  @Test
  void updatePomodoroSession_WithInvalidSessionType_ThrowsIllegalArgumentException() {
    String sessionType = "not_real_type";

    when(pomodoroSessionRepository.findById(pomodoroSessionId))
        .thenReturn(Optional.of(pomodoroSession));

    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () -> pomodoroSessionService.updatePomodoroSession(pomodoroSessionId, sessionType));

    assertEquals("Invalid session type " + sessionType, e.getMessage());
  }
}
