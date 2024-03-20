
package org.opensourcecommunity.pomodoroapp.controllers;

import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationRequestDto;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.RegisterRequestDto;
import org.opensourcecommunity.pomodoroapp.services.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthenticationController {

	private final AuthenticationService service;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequestDto request) {
		return ResponseEntity.ok(service.register(request));

	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationResponseDto> register(@RequestBody AuthenticationRequestDto request) {
		return ResponseEntity.ok(service.authenticate(request));

	}

}
