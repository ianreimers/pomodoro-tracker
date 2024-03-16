package org.opensourcecommunity.pomodoroapp.services;

import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationRequestDto;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.RegisterRequestDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

	private final UserRepository userRepository;
	private final UserSettingsService userSettingsService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponseDto register(RegisterRequestDto request) {
		User user = User
				.builder()
				.username(request.getUsername())
				.email(request.getEmail())
				.password(passwordEncoder.encode(request.getPassword()))
				.build();
		User savedUser = userRepository.save(user);
		userSettingsService.createUserSettings(savedUser);

		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponseDto.builder().token(jwtToken).build();
	}

	public AuthenticationResponseDto authenticate(AuthenticationRequestDto request) {

		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

		User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
		String jwtToken = jwtService.generateToken(user);
		return AuthenticationResponseDto.builder().token(jwtToken).build();
	}

	public boolean isUserAuthorized(User user, Authentication auth) {
		String username = auth.getName();

		return user != null && user.getUsername().equals(username);
	}

}
