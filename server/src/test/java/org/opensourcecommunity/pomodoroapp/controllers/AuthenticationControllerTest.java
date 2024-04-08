package org.opensourcecommunity.pomodoroapp.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.config.SecurityConfiguration;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationRequestDto;
import org.opensourcecommunity.pomodoroapp.dtos.AuthenticationResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.RegisterRequestDto;
import org.opensourcecommunity.pomodoroapp.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfiguration.class)
public class AuthenticationControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private AuthenticationService authenticationService;
  @MockBean private JwtService jwtService;
  @MockBean private AuthenticationProvider authenticationProvider;

  private AuthenticationRequestDto loginDto;
  private RegisterRequestDto registerDto;
  private AuthenticationResponseDto responseDto;

  @Autowired private ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    loginDto = new AuthenticationRequestDto("user", "password");
    registerDto = new RegisterRequestDto("user", "user@example.com", "password");
    responseDto = new AuthenticationResponseDto("Bearer token", "user");
  }

  @Test
  void register_WhenCalledWithValidData_ShouldReturnToken() throws Exception {
    when(authenticationService.register(any(RegisterRequestDto.class))).thenReturn(responseDto);

    String objectJson = objectMapper.writeValueAsString(registerDto);

    mockMvc
        .perform(
            post("/api/v1/register").contentType(MediaType.APPLICATION_JSON).content(objectJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").value("Bearer token"))
        .andExpect(jsonPath("$.username").value("user"))
        .andDo(print());

    verify(authenticationService).register(registerDto);
  }

  @Test
  void login_WhenCalledWithValidCredentials_ShouldReturnToken() throws Exception {
    when(authenticationService.authenticate(any(AuthenticationRequestDto.class)))
        .thenReturn(responseDto);

    String objectJson = objectMapper.writeValueAsString(loginDto);

    mockMvc
        .perform(post("/api/v1/login").contentType(MediaType.APPLICATION_JSON).content(objectJson))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.token").value("Bearer token"))
        .andExpect(jsonPath("$.username").value("user"))
        .andDo(print());

    verify(authenticationService).authenticate(loginDto);
  }
}
