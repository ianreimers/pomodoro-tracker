package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserSettingsNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserSettingsMapper;
import org.opensourcecommunity.pomodoroapp.models.Role;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;

public class UserSettingsServiceTest {

  @Mock private UserSettingsRepository userSettingsRepository;
  @Mock private UserSettingsMapper userSettingsMapper;

  @InjectMocks private UserSettingsService userSettingsService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  private User createUser(Long userId) {
    return User.builder()
        .id(userId)
        .username("user")
        .email("user@example.com")
        .password("password")
        .role(Role.USER)
        .build();
  }

  private UserSettings createUserSettings(Long userSettingsId, User user) {
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

  @Test
  void getUserSettingsById_WithValidId_ReturnUserSettings() {
    // Arrange
    Long userSettingsId = 1L;
    Long userId = 1L;
    User user = createUser(userId);
    UserSettings userSettings = createUserSettings(userSettingsId, user);

    when(userSettingsRepository.findById(userSettingsId)).thenReturn(Optional.of(userSettings));

    // Act
    UserSettings responseUserSettings = userSettingsService.getUserSettingById(userSettingsId);

    // Assert
    assertEquals(userSettings.getId(), responseUserSettings.getId());
    assertEquals(userSettings.getUser(), responseUserSettings.getUser());
    assertEquals(userSettings.getTaskSeconds(), responseUserSettings.getTaskSeconds());
    assertEquals(userSettings.getShortBreakSeconds(), responseUserSettings.getShortBreakSeconds());
    assertEquals(userSettings.getLongBreakSeconds(), responseUserSettings.getLongBreakSeconds());
    assertEquals(userSettings.getPomodoroInterval(), responseUserSettings.getPomodoroInterval());
    assertEquals(userSettings.getSound(), responseUserSettings.getSound());
    verify(userSettingsRepository, times(1)).findById(userSettingsId);
  }

  @Test
  void getUserSettingsById_WithNullId_ThrowsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> userSettingsService.getUserSettingById(null));

    assertEquals("User settings id should not be null", exception.getMessage());
  }

  @Test
  void getUserSettingsById_WithInvalidId_ThrowUserSettingsNotFoundException() {
    UserSettingsNotFoundException exception =
        assertThrows(
            UserSettingsNotFoundException.class, () -> userSettingsService.getUserSettingById(1L));

    assertEquals(exception.getMessage(), "Could not find user settings");
  }

  @Test
  void getUserSettingsByUser_WithValidUser_ReturnUserSettingsDto() {
    // Arrange
    Long userSettingsId = 1L;
    Long userId = 5L;
    User user = createUser(userId);
    UserSettings userSettings = createUserSettings(userSettingsId, user);
    user.setUserSettings(userSettings);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound());

    when(userSettingsMapper.userSettingsToUserSettingsDto(userSettings)).thenReturn(dto);

    // Act
    UserSettingsDto responseUserSettings = userSettingsService.getUserSettingsByUser(user);

    // Assert
    assertNotNull(responseUserSettings);
    assertEquals(userSettings.getTaskSeconds(), responseUserSettings.taskSeconds());
    assertEquals(userSettings.getShortBreakSeconds(), responseUserSettings.shortBreakSeconds());
    assertEquals(userSettings.getLongBreakSeconds(), responseUserSettings.longBreakSeconds());
    assertEquals(userSettings.getPomodoroInterval(), responseUserSettings.pomodoroInterval());
    assertEquals(userSettings.getSound(), responseUserSettings.sound());
    verify(userSettingsMapper, times(1)).userSettingsToUserSettingsDto(userSettings);
  }

  @Test
  void getUserSettingsByUser_WithNullUser_ThrowsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> userSettingsService.getUserSettingsByUser(null));

    assertEquals("User should not be null", exception.getMessage());
  }

  @Test
  void getUserSettingsByUser_WithNullUserSettings_ThrowsUserSettingsNotFoundException() {
    User user = createUser(1L);

    UserSettingsNotFoundException exception =
        assertThrows(
            UserSettingsNotFoundException.class,
            () -> userSettingsService.getUserSettingsByUser(user));

    assertEquals(
        "Could not find user settings for user " + user.getUsername(), exception.getMessage());
  }

  @Test
  void createUserSettings_WithValidUser_ReturnUserSettings() {
    // Arrange
    Long userId = 1L;
    Long userSettingsId = 5L;
    User user = createUser(userId);
    UserSettings userSettings = createUserSettings(userSettingsId, user);

    when(userSettingsRepository.save(any(UserSettings.class)))
        .thenAnswer(
            i -> {
              UserSettings savedSettings = i.getArgument(0, UserSettings.class);
              if (savedSettings.getId() == null) savedSettings.setId(userSettingsId);
              return savedSettings;
            });

    // Act
    UserSettings responseUserSettings = userSettingsService.createUserSettings(user);

    // Assert
    assertNotNull(responseUserSettings);
    assertEquals(5L, responseUserSettings.getId());
    assertEquals(1500, responseUserSettings.getTaskSeconds());
    assertEquals(600, responseUserSettings.getShortBreakSeconds());
    assertEquals(1800, responseUserSettings.getLongBreakSeconds());
    assertEquals(4, responseUserSettings.getPomodoroInterval());
    assertEquals("bells", responseUserSettings.getSound());
    verify(userSettingsRepository, times(1)).save(userSettings);
  }

  @Test
  void createUserSettings_WithNullUser_ThrowsIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> userSettingsService.getUserSettingsByUser(null));

    assertEquals("User should not be null", exception.getMessage());
  }

  @Test
  void userSettingsToUserSettingsDto_WithValidUserSettings_ReturnUserSettingsDto() {
    // Arrange
    Long userId = 1L;
    Long userSettingsId = 5L;
    User user = createUser(userId);
    UserSettings userSettings = createUserSettings(userSettingsId, user);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound());

    when(userSettingsMapper.userSettingsToUserSettingsDto(userSettings)).thenReturn(dto);

    // Act
    UserSettingsDto responseDto = userSettingsService.userSettingsToUserSettingsDto(userSettings);

    // Assert
    assertNotNull(responseDto);
    assertEquals(userSettings.getTaskSeconds(), responseDto.taskSeconds());
    assertEquals(userSettings.getShortBreakSeconds(), responseDto.shortBreakSeconds());
    assertEquals(userSettings.getLongBreakSeconds(), responseDto.longBreakSeconds());
    assertEquals(userSettings.getPomodoroInterval(), responseDto.pomodoroInterval());
    assertEquals(userSettings.getSound(), responseDto.sound());
    verify(userSettingsMapper, times(1)).userSettingsToUserSettingsDto(userSettings);
  }

  @Test
  void userSettingsToUserSettingsDto_WithNullUserSettings_ThrowIllegalArgumentException() {
    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> userSettingsService.userSettingsToUserSettingsDto(null));

    assertEquals("User settings should not be null", exception.getMessage());
  }

  @Test
  void updateUserSettings_WithValidUserAndUserSettingsDto_ReturnUserSettingsDto() {
    Long userId = 1L;
    Long userSettingsId = 5L;
    User user = createUser(userId);
    UserSettings userSettings = createUserSettings(userSettingsId, user);
    user.setUserSettings(userSettings);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound());

    when(userSettingsRepository.findByUser(user)).thenReturn(Optional.of(userSettings));
    when(userSettingsRepository.save(userSettings)).thenReturn(userSettings);
    when(userSettingsMapper.userSettingsToUserSettingsDto(userSettings)).thenReturn(dto);

    UserSettingsDto responseDto = userSettingsService.updateUserSettings(user, dto);

    assertNotNull(responseDto);
    assertEquals(userSettings.getTaskSeconds(), responseDto.taskSeconds());
    assertEquals(userSettings.getShortBreakSeconds(), responseDto.shortBreakSeconds());
    assertEquals(userSettings.getLongBreakSeconds(), responseDto.longBreakSeconds());
    assertEquals(userSettings.getPomodoroInterval(), responseDto.pomodoroInterval());
    assertEquals(userSettings.getSound(), responseDto.sound());
    verify(userSettingsMapper, times(1)).userSettingsToUserSettingsDto(userSettings);
  }

  @Test
  void updateUserSettings_WithNullUserSettings_ThrowsUserSettingsNotFoundException() {
    User user = createUser(1L);
    UserSettings userSettings = createUserSettings(5L, user);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound());
    UserSettingsNotFoundException exception =
        assertThrows(
            UserSettingsNotFoundException.class,
            () -> userSettingsService.updateUserSettings(user, dto));

    assertEquals(
        "Could not find user settings for user " + user.getUsername(), exception.getMessage());
  }

  @Test
  void updateUserSettings_WithNullUserOrNullUserSettingsDto_ThrowsIllegalArgumentException() {
    User user = createUser(1L);

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> userSettingsService.updateUserSettings(null, null));
    assertEquals("User and user settings dto should not be null", exception.getMessage());

    exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> userSettingsService.updateUserSettings(user, null));
    assertEquals("User and user settings dto should not be null", exception.getMessage());
  }
}
