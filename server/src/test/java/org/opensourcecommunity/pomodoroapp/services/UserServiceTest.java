package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserMapper userMapper;
  @InjectMocks private UserService userService;

  private Long userId;
  private User user;
  private UserDto userDto;
  private UserResponseDto userResponseDto;

  @BeforeEach
  void setUp() {
    userId = 1L;
    user = TestDataFactory.createUser(userId);
    userDto = TestDataFactory.createUserDto(user);
    userResponseDto = TestDataFactory.createUserResponseDto(user);
  }

  @Test
  void getAllUsers_UserExist_ReturnUserList() {
    // Arrange
    List<User> users = Arrays.asList(user);

    when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);
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
    when(userMapper.userDtoToUser(userDto)).thenReturn(user);
    when(userRepository.save(eq(user))).thenReturn(user);

    // Act
    User responseDto = userService.createUser(userDto);

    // Assert
    assertNotNull(responseDto);
    assertEquals(user.getId(), responseDto.getId());
    assertEquals(user.getUsername(), responseDto.getUsername());
    assertEquals(user.getEmail(), responseDto.getEmail());
    assertEquals(user.getPassword(), responseDto.getPassword());

    verify(userMapper, times(1)).userDtoToUser(userDto);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void getUserResponseDtoById_ValidId_ReturnUserResponseDto() {
    when(userRepository.findById(userId)).thenReturn(Optional.of(user));
    when(userMapper.userToUserResponseDto(user)).thenReturn(userResponseDto);

    // Act
    UserResponseDto responseDto = userService.getUserResponseDtoById(userId);

    // Assert
    assertNotNull(responseDto);
    assertEquals(user.getId(), userResponseDto.id());
    assertEquals(user.getUsername(), userResponseDto.username());
    assertEquals(user.getEmail(), userResponseDto.email());
    assertEquals(user.getUserSettings(), userResponseDto.userSettings());
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
    when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

    User responseUser = userService.getUserByUsername(user.getUsername());

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
