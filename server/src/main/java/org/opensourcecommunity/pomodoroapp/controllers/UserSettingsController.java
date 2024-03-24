package org.opensourcecommunity.pomodoroapp.controllers;

import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.opensourcecommunity.pomodoroapp.services.UserSettingsService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class UserSettingsController {

	private final UserSettingsService userSettingsService;
	private final UserService userService;

	public UserSettingsController(UserSettingsService userSettingsService, UserService userService) {
		this.userSettingsService = userSettingsService;
		this.userService = userService;
	}

	@GetMapping("/settings")
	public UserSettingsDto getUserSettings(Authentication auth) {
		User user = (User) auth.getPrincipal();
		return userSettingsService.getUserSettingsByUser(user);
	}

	@PutMapping("/settings")
	public UserSettingsDto updateUserSettings(@Valid @RequestBody UserSettingsDto dto, Authentication auth) {
		User user = (User) auth.getPrincipal();
		// User user = userService.getUserByUsername(auth.getName());
		return userSettingsService.updateUserSettings(user, dto);

	}

}
