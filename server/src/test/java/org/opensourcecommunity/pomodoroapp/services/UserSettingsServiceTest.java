package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserSettingsNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserSettingsMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class UserSettingsServiceTest {

  @Mock private UserSettingsRepository userSettingsRepository;
  @Mock private UserSettingsMapper userSettingsMapper;

  @InjectMocks private UserSettingsService userSettingsService;

  private Long userSettingsId = 1L;
  private Long userId = 1L;
  private User user;
  private UserSettings userSettings;

  @BeforeEach
  void setUp() {
    user = TestDataFactory.createUser(userId);
    userSettings = TestDataFactory.createUserSettings(userSettingsId, user);
  }

  @Test
  void getUserSettingsById_WithValidId_ReturnUserSettings() {
    // Arrange

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
    assertEquals(userSettings.getSoundVolume(), responseUserSettings.getSoundVolume());
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
    user.setUserSettings(userSettings);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound(),
            userSettings.getSoundVolume());

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
    assertEquals(userSettings.getSoundVolume(), responseUserSettings.soundVolume());
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
    UserSettingsNotFoundException exception =
        assertThrows(
            UserSettingsNotFoundException.class,
            () -> userSettingsService.getUserSettingsByUser(user));

    assertEquals(
        "Could not find user settings for user " + user.getUsername(), exception.getMessage());
  }

  @Test
  void createUserSettings_WithValidUser_ReturnUserSettings() {
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
    assertEquals(1L, responseUserSettings.getId());
    assertEquals(1500, responseUserSettings.getTaskSeconds());
    assertEquals(600, responseUserSettings.getShortBreakSeconds());
    assertEquals(1800, responseUserSettings.getLongBreakSeconds());
    assertEquals(4, responseUserSettings.getPomodoroInterval());
    assertEquals("bells", responseUserSettings.getSound());
    assertEquals(100, responseUserSettings.getSoundVolume());
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
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound(),
            userSettings.getSoundVolume());

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
    assertEquals(userSettings.getSoundVolume(), responseDto.soundVolume());
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
    user.setUserSettings(userSettings);
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound(),
            userSettings.getSoundVolume());

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
    assertEquals(userSettings.getSoundVolume(), responseDto.soundVolume());
    verify(userSettingsMapper, times(1)).userSettingsToUserSettingsDto(userSettings);
  }

  @Test
  void updateUserSettings_WithNullUserSettings_ThrowsUserSettingsNotFoundException() {
    UserSettingsDto dto =
        new UserSettingsDto(
            userSettings.getTaskSeconds(),
            userSettings.getShortBreakSeconds(),
            userSettings.getLongBreakSeconds(),
            userSettings.getPomodoroInterval(),
            userSettings.getSound(),
            userSettings.getSoundVolume());
    UserSettingsNotFoundException exception =
        assertThrows(
            UserSettingsNotFoundException.class,
            () -> userSettingsService.updateUserSettings(user, dto));

    assertEquals(
        "Could not find user settings for user " + user.getUsername(), exception.getMessage());
  }

  @Test
  void updateUserSettings_WithNullUserOrNullUserSettingsDto_ThrowsIllegalArgumentException() {
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
