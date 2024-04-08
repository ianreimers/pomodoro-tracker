package org.opensourcecommunity.pomodoroapp.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationRequestDto;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.RegisterRequestDto;
import org.opensourcecommunity.pomodoroapp.exceptions.InvalidCredentialsException;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

  @Mock private UserRepository userRepository;
  @Mock private UserSettingsService userSettingsService;
  @Mock private PasswordEncoder passwordEncoder;
  @Mock private JwtService jwtService;
  @Mock private AuthenticationManager authenticationManager;

  @InjectMocks private AuthenticationService authenticationService;

  @Test
  void register_WithValidRequest_ReturnTokenAndUsername() {
    RegisterRequestDto registerDto = new RegisterRequestDto("user", "user@exmaple.com", "password");
    String jwtToken = "Jwt token";
    String encodedPassword = "Encoded password";
    User user =
        User.builder()
            .username(registerDto.getUsername())
            .email(registerDto.getEmail())
            .password(encodedPassword)
            .build();
    UserSettings userSettings = TestDataFactory.createUserSettings(user.getId(), user);

    when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
    when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
    when(userRepository.save(any(User.class))).thenReturn(user);
    when(passwordEncoder.encode(registerDto.getPassword())).thenReturn(encodedPassword);
    when(userSettingsService.createUserSettings(user)).thenReturn(userSettings);
    when(jwtService.generateToken(user)).thenReturn(jwtToken);

    AuthenticationResponseDto responseDto = authenticationService.register(registerDto);

    assertEquals(user.getUsername(), responseDto.getUsername());
    assertEquals(jwtToken, responseDto.getToken());

    verify(userRepository).save(user);
    verify(userSettingsService).createUserSettings(user);
    verify(jwtService).generateToken(user);
    verify(passwordEncoder).encode(registerDto.getPassword());
  }

  @Test
  void register_WithExistingUsername_ThrowsException() {
    RegisterRequestDto registerDto = new RegisterRequestDto("user", "user@exmaple.com", "password");

    when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);

    Exception e =
        assertThrows(
            IllegalArgumentException.class, () -> authenticationService.register(registerDto));

    assertEquals("Username already exists", e.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void register_WithExistingEmail_ThrowsException() {
    RegisterRequestDto registerDto = new RegisterRequestDto("user", "user@exmaple.com", "password");

    when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);

    Exception e =
        assertThrows(
            IllegalArgumentException.class, () -> authenticationService.register(registerDto));

    assertEquals("Email already exists", e.getMessage());
    verify(userRepository, never()).save(any(User.class));
  }

  @Test
  void authenticate_WithValidCredentials_ReturnTokenAndUsername() {
    AuthenticationRequestDto loginDto = new AuthenticationRequestDto("user", "password");
    User user = TestDataFactory.createUser();
    String jwtToken = "Jwt token";

    when(userRepository.findByUsername(loginDto.getUsername())).thenReturn(Optional.of(user));
    when(jwtService.generateToken(user)).thenReturn(jwtToken);
    when(authenticationManager.authenticate(any(Authentication.class)))
        .thenReturn(any(Authentication.class));

    AuthenticationResponseDto responseDto = authenticationService.authenticate(loginDto);

    assertEquals(jwtToken, responseDto.getToken());
    assertEquals("user", responseDto.getUsername());

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(jwtService).generateToken(user);
  }

  @Test
  void authenticate_WithInvalidCredentials_ThrowsInvalidCredentialsException() {
    AuthenticationRequestDto loginDto = new AuthenticationRequestDto("user", "wrongpassword");

    doThrow(new BadCredentialsException("Invalid credentials"))
        .when(authenticationManager)
        .authenticate(any(UsernamePasswordAuthenticationToken.class));

    assertThrows(
        InvalidCredentialsException.class, () -> authenticationService.authenticate(loginDto));

    verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    verify(userRepository, never()).findByUsername(anyString());
  }
}
