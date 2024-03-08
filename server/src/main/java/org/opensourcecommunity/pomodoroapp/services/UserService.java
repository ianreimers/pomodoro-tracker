
package org.opensourcecommunity.pomodoroapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.mappers.UserMapper;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserSettingsRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {
	private final UserRepository userRepository;
	private final UserSettingsRepository userSettingsRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper,
			UserSettingsRepository userSettingsRepository) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
		this.userSettingsRepository = userSettingsRepository;
	}

	public List<UserResponseDto> getAllUsers() {
		List<User> users = userRepository.findAll();
		return users.stream().map(user -> userMapper.userToUserResponseDto(user)).collect(Collectors.toList());
	}

	public UserResponseDto createUser(UserDto userDto) {
		User user = userMapper.userDtoToUser(userDto);

		User savedUser = userRepository.save(user);

		// Create the user settings using db defaults
		UserSettings userSettings = new UserSettings();
		userSettings.setUser(user);

		// Attach it to the saved user that gets returned
		UserSettings savedUserSettings = userSettingsRepository.save(userSettings);
		savedUser.setUserSettings(savedUserSettings);

		return userMapper.userToUserResponseDto(savedUser);
	}

	public UserResponseDto getUserById(Long id) {
		User user = userRepository.findById(id).orElse(null);
		return userMapper.userToUserResponseDto(user);

	}

	public UserSettingsDto updateUserSettings(Long userId, UserSettingsDto dto) {
		UserSettings userSettings = userSettingsRepository.findByUserId(userId);
		userSettings.setStudyTime(dto.studyTime());
		userSettings.setLongBreakTime(dto.longBreakTime());
		userSettings.setShortBreakTime(dto.shortBreakTime());

		UserSettings updatedUserSettings = userSettingsRepository.save(userSettings);

		return userMapper.userSettingsToUserSettingsDto(updatedUserSettings);
	}

	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);

	}

	public void deleteUserById(Long id) {
		userRepository.deleteById(id);
	}

}
