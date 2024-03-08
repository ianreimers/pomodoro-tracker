package org.opensourcecommunity.pomodoroapp.controllers;

import java.util.HashMap;
import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/users")
	public List<UserResponseDto> getAllUsers() {
		return userService.getAllUsers();
	}

	@PostMapping("/users")
	public UserResponseDto createUser(@Valid @RequestBody UserDto userDto) {
		return userService.createUser(userDto);
	}

	@PutMapping("/users/{id}/settings")
	public UserSettingsDto updateUserSettings(@PathVariable(name = "id") Long userId,
			@Valid @RequestBody UserSettingsDto dto) {
		return userService.updateUserSettings(userId, dto);

	}

	@GetMapping("/users/{id}")
	public UserResponseDto getUserById(@PathVariable Long id) {
		return userService.getUserById(id);
	}

	// TODO: handle cases where entity does not exisit instead of return NO_CONTENT
	//
	@DeleteMapping("/users/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteUser(@PathVariable Long id) {
		userService.deleteUserById(id);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
		var errors = new HashMap<String, String>();
		exp.getBindingResult().getAllErrors().forEach(error -> {
			var fieldName = ((FieldError) error).getField();
			var errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);

		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);

	}

}
