package org.opensourcecommunity.pomodoroapp.controllers;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionPatchDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.exceptions.ForbiddenException;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.services.PomodoroSessionService;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class PomodoroSessionController {

	private final PomodoroSessionService pomodoroSessionService;

	public PomodoroSessionController(PomodoroSessionService pomodoroSessionService) {
		this.pomodoroSessionService = pomodoroSessionService;
	}

	@GetMapping("/pomodoro-sessions")
	public ResponseEntity<List<PomodoroSessionResponseDto>> getUserPomodoroSessions(Authentication auth) {

		List<PomodoroSessionResponseDto> sessions = pomodoroSessionService
				.getPomodoroSessionsByUser(auth.getName());

		return ResponseEntity.ok(sessions);
	}

	@PostMapping("/pomodoro-sessions")
	public ResponseEntity<PomodoroSessionResponseDto> createPomodoroSession(
			@Valid @RequestBody PomodoroSessionDto dto, Authentication auth) {

		PomodoroSessionResponseDto pomodoroReturn = pomodoroSessionService.createPomodoroSession(auth.getName(),
				dto);

		return ResponseEntity.status(HttpStatus.CREATED).body(pomodoroReturn);
	}

	@PatchMapping("/pomodoro-sessions/{pomodoro-session-id}")
	public ResponseEntity<?> updatePomodoroSession(
			@PathVariable("pomodoro-session-id") Long pomodoroId,
			@RequestBody PomodoroSessionPatchDto dto,
			Authentication auth) {

		pomodoroSessionService.updatePomodoroSession(pomodoroId, dto.durationType());

		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
