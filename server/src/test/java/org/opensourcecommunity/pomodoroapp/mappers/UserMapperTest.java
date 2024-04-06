package org.opensourcecommunity.pomodoroapp.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;

public class UserMapperTest {
  UserMapper mapper;

  @BeforeEach
  void setUp() {
    mapper = new UserMapper();
  }

  @Test
  void shouldMapUserToUserResponseDto() {
    // Arrange
    UserSettings userSettings = UserSettings.builder().build();

    User user =
        User.builder()
            .id(1L)
            .username("testusername")
            .password("password")
            .email("example@email.com")
            .userSettings(userSettings)
            .build();

    // Act
    UserResponseDto resultDto = mapper.userToUserResponseDto(user);

    // Assert
    assertEquals(user.getUsername(), resultDto.username());
    assertEquals(user.getEmail(), resultDto.email());
    assertEquals(user.getId(), resultDto.id());
    assertEquals(user.getUserSettings(), resultDto.userSettings());
  }

  @Test
  public void userToUserResponseDto_NullUser_ThrowsNullPointerException() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> mapper.userToUserResponseDto(null));

    assertEquals("The user should not be null", exception.getMessage());
  }

  @Test
  void shouldMapUserToUserDto() {
    // Arrange
    UserSettings userSettings = UserSettings.builder().build();
    User user =
        User.builder()
            .id(1L)
            .username("testusername")
            .password("password")
            .email("example@email.com")
            .userSettings(userSettings)
            .build();

    UserDto dto = new UserDto("testusername", "example@email.com", "password");

    // Act
    UserDto resultDto = mapper.userToUserDto(user);

    // Assert
    assertEquals(resultDto, dto);
  }

  @Test
  void userToUserDto_NullUser_ThrowsNullPointerException() {
    NullPointerException exception =
        assertThrows(NullPointerException.class, () -> mapper.userToUserDto(null));

    assertEquals("The user should not be null", exception.getMessage());
  }

  @Test
  void shouldMapUserDtoToUser() {
    // Arrange
    User user =
        User.builder()
            .id(1L)
            .username("testusername")
            .password("password")
            .email("example@email.com")
            .build();

    UserDto dto = new UserDto("testusername", "example@email.com", "password");

    // Act
    User resultUser = mapper.userDtoToUser(dto);

    assertEquals(user.getUsername(), resultUser.getUsername());
    assertEquals(user.getPassword(), resultUser.getPassword());
    assertEquals(user.getEmail(), resultUser.getEmail());
  }

  @Test
  void userDtoToUser_NullUser_ThrowsNullPointerException() {
    var msg = assertThrows(NullPointerException.class, () -> mapper.userDtoToUser(null));

    assertEquals(msg.getMessage(), "The user should not be null");
  }
}
