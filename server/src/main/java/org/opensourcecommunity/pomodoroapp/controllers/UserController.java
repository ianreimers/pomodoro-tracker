package org.opensourcecommunity.pomodoroapp.controllers;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.opensourcecommunity.pomodoroapp.services.UserSettingsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	private final UserService userService;

	private final UserSettingsService userSettingsService;

	public UserController(UserService userService, UserSettingsService userSettingsService) {
		this.userService = userService;
		this.userSettingsService = userSettingsService;
	}

	@GetMapping("/users")
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@GetMapping("/users/{id}")
	public UserResponseDto getUserById(@PathVariable Long id) {
		return userService.getUserResponseDtoById(id);
	}

	@PostMapping("/users")
	public UserResponseDto createUser(@Valid @RequestBody UserDto userDto) {
		// Create a new user
		User savedUser = userService.createUser(userDto);
		// Attached default user settings to it
		userSettingsService.createUserSettings(savedUser);

		return userService.convertUserToUserResponseDto(savedUser);
	}

	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
	}

}
