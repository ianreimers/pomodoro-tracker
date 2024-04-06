package org.opensourcecommunity.pomodoroapp.services;

import java.util.List;
import java.util.stream.Collectors;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserNotFoundException;
import org.opensourcecommunity.pomodoroapp.mappers.UserMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public UserService(UserRepository userRepository, UserMapper userMapper) {
    this.userRepository = userRepository;
    this.userMapper = userMapper;
  }

  public List<UserResponseDto> getAllUsers() {
    List<User> users = userRepository.findAll();
    return users.stream()
        .map(user -> userMapper.userToUserResponseDto(user))
        .collect(Collectors.toList());
  }

  public User createUser(UserDto userDto) {
    // Create a new user with the given user information
    User user = userMapper.userDtoToUser(userDto);
    User savedUser = userRepository.save(user);
    return savedUser;
  }

  public UserResponseDto convertUserToUserResponseDto(User user) {
    return userMapper.userToUserResponseDto(user);
  }

  public UserResponseDto getUserResponseDtoById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("User does not exist with id " + id));
    return userMapper.userToUserResponseDto(user);
  }

  public User getUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User does not exist with id " + id));
  }

  public User getUserByUsername(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(
            () -> new UserNotFoundException("User does not exist with username " + username));
  }

  public void deleteUserById(Long id) {
    if (!userRepository.existsById(id)) {
      throw new UserNotFoundException("User does not exist, unable to delete");
    }

    userRepository.deleteById(id);
  }
}
