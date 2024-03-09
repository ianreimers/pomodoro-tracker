package org.opensourcecommunity.pomodoroapp.controllers;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionPatchDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.services.PomodoroSessionService;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class PomodoroSessionController {

	PomodoroSessionService pomodoroSessionService;
	UserService userService;

	public PomodoroSessionController(PomodoroSessionService pomodoroSessionService, UserService userService) {
		this.pomodoroSessionService = pomodoroSessionService;
		this.userService = userService;
	}

	@GetMapping("/users/pomodoro-sessions")
	public List<PomodoroSession> getAllPomdoroSessions() {
		return pomodoroSessionService.getAllPomodoroSessions();
	}

	@GetMapping("/users/{id}/pomodoro-sessions")
	public List<PomodoroSession> getUserPomodoroSessions(@PathVariable Long id) {
		return pomodoroSessionService.getPomodoroSessionsByUserId(id);
	}

	@PostMapping("/users/{id}/pomodoro-sessions")
	public PomodoroSessionResponseDto createPomodoroSession(
			@PathVariable Long id,
			@Valid @RequestBody PomodoroSessionDto dto) {
		// Frist, get the user by id to pass to the pomodoroSessionService
		User user = userService.getUserById(id);

		return pomodoroSessionService.createPomodoroSession(user, dto);
	}

	@PatchMapping("/users/{user-id}/pomodoro-sessions/{pomodoro-session-id}")
	public void updatePomodoroSession(
			@PathVariable(name = "user-id") Long userId,
			@PathVariable("pomodoro-session-id") Long pomodoroId,
			@RequestBody PomodoroSessionPatchDto dto) {
		pomodoroSessionService.updatePomodoroSession(userId, pomodoroId, dto.durationType());
	}

}
