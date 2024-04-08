package org.opensourcecommunity.pomodoroapp.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

public class UserMapperTest {
  private UserMapper mapper;
  private User user;
  private UserSettings userSettings;
  private UserDto dto;

  @BeforeEach
  void setUp() {
    mapper = new UserMapper();
    user = TestDataFactory.createUser();
    userSettings = TestDataFactory.createUserSettings(user.getId(), user);
    user.setUserSettings(userSettings);
    dto = new UserDto(user.getUsername(), user.getEmail(), user.getPassword());
  }

  @Test
  void userToUserResponseDto_WhenUserIsValid_ReturnUserResponseDto() {
    // Act
    UserResponseDto resultDto = mapper.userToUserResponseDto(user);

    // Assert
    assertNotNull(resultDto);
    assertEquals(user.getUsername(), resultDto.username());
    assertEquals(user.getEmail(), resultDto.email());
    assertEquals(user.getId(), resultDto.id());
    assertEquals(user.getUserSettings(), resultDto.userSettings());
  }

  @Test
  void userToUserResponseDto_WhenUserIsNull_ThrowNullPointerException() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> mapper.userToUserResponseDto(null));

    assertEquals("The user should not be null", exception.getMessage());
  }

  @Test
  void userToUserDto_WhenUserIsValid_ReturnUserDto() {
    // Act
    UserDto resultDto = mapper.userToUserDto(user);

    // Assert
    assertNotNull(resultDto);
    assertEquals(user.getUsername(), resultDto.username());
    assertEquals(user.getEmail(), resultDto.email());
    assertEquals(user.getPassword(), resultDto.password());
  }

  @Test
  void userToUserDto_WhenUserIsNull_ThrowNullPointerException() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> mapper.userToUserDto(null));

    assertEquals("The user should not be null", exception.getMessage());
  }

  @Test
  void userDtoToUser_WhenUserDtoIsValid_ReturnUser() {
    // Act
    User resultUser = mapper.userDtoToUser(dto);

    assertNotNull(resultUser);
    assertEquals(user.getUsername(), resultUser.getUsername());
    assertEquals(user.getPassword(), resultUser.getPassword());
    assertEquals(user.getEmail(), resultUser.getEmail());
  }

  @Test
  void userDtoToUser_WhenUserIsNull_ThrowNullPointerException() {
    var msg = assertThrows(NullPointerException.class, () -> mapper.userDtoToUser(null));

    assertEquals(msg.getMessage(), "The user should not be null");
  }
}
