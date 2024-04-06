package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;

public class UserServiceTest {

  @Mock private UserRepository userRepository;

  @Mock private UserMapper userMapper;

  @InjectMocks private UserService userService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllUsers_UserExist_ReturnUserList() {
    // Arrange
    User user =
        User.builder()
            .id(1L)
            .username("user")
            .email("user@example.com")
            .password("password")
            .build();
    List<User> users = Arrays.asList(user);

    when(userMapper.userToUserResponseDto(user))
        .thenReturn(new UserResponseDto(1L, "user", "user@example.com", null));
    when(userRepository.findAll()).thenReturn(users);

    // Act
    List<UserResponseDto> responseUsers = userService.getAllUsers();

    // Assert
    assertNotNull(responseUsers);
    assertEquals(1, responseUsers.size());
    verify(userRepository, times(1)).findAll();
    verify(userMapper, times(1)).userToUserResponseDto(user);
  }

  @Test
  void createUser_ValidUserDto_ReturnUser() {
    // Arrange
    User user =
        User.builder().username("dale").email("dale@example.com").password("password").build();
    UserDto dto = new UserDto("dale", "dale@example.com", "password");
    User savedUser =
        User.builder()
            .id(1L)
            .username("dale")
            .email("dale@example.com")
            .password("password")
            .build();

    when(userMapper.userDtoToUser(dto)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(savedUser);

    // Act
    User responseDto = userService.createUser(dto);

    // Assert
    assertNotNull(responseDto);
    assertEquals(savedUser.getId(), responseDto.getId());
    assertEquals(savedUser.getUsername(), responseDto.getUsername());
    assertEquals(savedUser.getEmail(), responseDto.getEmail());
    assertEquals(savedUser.getPassword(), responseDto.getPassword());

    verify(userMapper, times(1)).userDtoToUser(dto);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void convertUserToUserResponseDto_UserExist_ReturnUserResponseDto() {
    // Arrange
    User user =
        User.builder()
            .id(1L)
            .username("dale")
            .email("dale@example.com")
            .password("password")
            .build();
    UserResponseDto dto = new UserResponseDto(1L, "dale", "dale@example.com", null);

    when(userMapper.userToUserResponseDto(user)).thenReturn(dto);

    // Act
    assertEquals(user.getId(), dto.id());
    assertEquals(user.getUsername(), dto.username());
    assertEquals(user.getEmail(), dto.email());
    assertEquals(user.getUserSettings(), dto.userSettings());
  }

  @Test
  void getUserResponseDtoById_ValidId_ReturnUserResponseDto() {
    // Arrange
    Long userId = 1L;
    User user =
        User.builder()
            .id(userId)
            .username("dale")
            .email("dale@example.com")
            .password("password")
            .build();
    UserResponseDto dto = new UserResponseDto(1L, "dale", "dale@example.com", null);

    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.userToUserResponseDto(user)).thenReturn(dto);

    // Act
    UserResponseDto responseDto = userService.getUserResponseDtoById(userId);

    // Assert
    assertNotNull(responseDto);
    assertEquals(user.getId(), dto.id());
    assertEquals(user.getUsername(), dto.username());
    assertEquals(user.getEmail(), dto.email());
    assertEquals(user.getUserSettings(), dto.userSettings());
  }

  @Test
  void getUserResponseDtoById_WithInvalidId_ThrowUserNotFoundException() {
    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.getUserResponseDtoById(1L));

    assertEquals(exception.getMessage(), "User does not exist with id 1");
  }

  @Test
  void getUserById_WithValidUserId_ReturnUser() {
    Long userId = 1L;
    User user =
        User.builder()
            .id(userId)
            .username("dale")
            .email("dale@example.com")
            .password("password")
            .build();
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));

    User responseUser = userService.getUserById(userId);

    assertEquals(user.getId(), responseUser.getId());
    assertEquals(user.getUsername(), responseUser.getUsername());
    assertEquals(user.getEmail(), responseUser.getEmail());
    assertEquals(user.getUserSettings(), responseUser.getUserSettings());
  }

  @Test
  void getUserById_WithInvalidId_ThrowUserNotFoundException() {
    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));

    assertEquals(exception.getMessage(), "User does not exist with id 1");
  }

  @Test
  void getUserByUsername_UsernameExists_ReturnUser() {
    User user =
        User.builder()
            .id(1L)
            .username("dale")
            .email("dale@example.com")
            .password("password")
            .build();
    when(userRepository.findByUsername("dale")).thenReturn(Optional.of(user));

    User responseUser = userService.getUserByUsername("dale");

    assertEquals(user.getId(), responseUser.getId());
    assertEquals(user.getUsername(), responseUser.getUsername());
    assertEquals(user.getEmail(), responseUser.getEmail());
    assertEquals(user.getUserSettings(), responseUser.getUserSettings());
  }

  @Test
  void getUserByUsername_WithInvalidUsername_ThrowUserNotFoundException() {
    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("dale"));

    assertEquals(exception.getMessage(), "User does not exist with username dale");
  }

  @Test
  void deleteUserById_UserExists_DeleteUser() {
    Long userId = 1L;
    when(userRepository.existsById(userId)).thenReturn(true);

    userService.deleteUserById(userId);

    verify(userRepository).deleteById(userId);
    verify(userRepository, times(1)).deleteById(userId);
  }

  @Test
  void deleteUserById_UserDoesNotExist_ThrowUserNotFoundException() {
    UserNotFoundException exception =
        assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(1L));

    assertEquals("User does not exist, unable to delete", exception.getMessage());
  }
}
