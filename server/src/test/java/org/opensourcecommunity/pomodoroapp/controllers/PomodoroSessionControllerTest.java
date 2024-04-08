package org.opensourcecommunity.pomodoroapp.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.config.SecurityConfiguration;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionPatchDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.services.PomodoroSessionService;
import org.opensourcecommunity.pomodoroapp.util.TestDataFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PomodoroSessionController.class)
@Import(SecurityConfiguration.class)
public class PomodoroSessionControllerTest {

  @MockBean private JwtService jwtService;
  @MockBean private AuthenticationProvider authenticationProvider;
  @MockBean private Authentication auth;
  @MockBean private PomodoroSessionService pomodoroSessionService;

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;

  private User user;

  @BeforeEach
  void setUp() {
    user = TestDataFactory.createUser();
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            user, null, AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(auth.getPrincipal()).thenReturn(user);
  }

  @Test
  void getUserPomodoroSessions_WhenAuthenticated_ShouldReturnPomodoroSessionResponseDtoList()
      throws Exception {
    PomodoroSession pomodoroSession = TestDataFactory.createPomodoroSession(user);
    PomodoroSessionResponseDto pomodoroSessionResponseDto =
        TestDataFactory.createPomodoroSessionResponseDto(pomodoroSession);
    List<PomodoroSessionResponseDto> dtos = Arrays.asList(pomodoroSessionResponseDto);

    when(pomodoroSessionService.getPomodoroSessionsByUser(user)).thenReturn(dtos);

    mockMvc
        .perform(get("/api/v1/pomodoro-sessions"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].taskDuration").value(pomodoroSession.getTaskDuration()))
        .andDo(print());

    verify(pomodoroSessionService).getPomodoroSessionsByUser(user);
  }

  @Test
  void createPomodoroSessions_WhenAuthenticated_ShouldReturnPomodoroSessionResponseDto()
      throws Exception {
    PomodoroSession pomodoroSession = TestDataFactory.createPomodoroSession(user);
    PomodoroSessionDto pomodoroSessionDto =
        TestDataFactory.createPomodoroSessionDto(pomodoroSession);
    PomodoroSessionResponseDto pomodoroSessionResponseDto =
        TestDataFactory.createPomodoroSessionResponseDto(pomodoroSession);

    when(pomodoroSessionService.createPomodoroSession(
            any(User.class), any(PomodoroSessionDto.class)))
        .thenReturn(pomodoroSessionResponseDto);

    String pomodoroSessionJson = objectMapper.writeValueAsString(pomodoroSessionDto);

    mockMvc
        .perform(
            post("/api/v1/pomodoro-sessions")
                .content(pomodoroSessionJson)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.taskDuration").value(pomodoroSession.getTaskDuration()))
        .andDo(print());

    verify(pomodoroSessionService)
        .createPomodoroSession(any(User.class), any(PomodoroSessionDto.class));
  }

  @Test
  void updatePomodoroSessions_WhenAuthenticated_ShouldReturnPomodoroSessionResponseDto()
      throws Exception {
    Long pomodoroId = 1L;
    PomodoroSessionPatchDto pomodoroSessionPatchDto =
        TestDataFactory.createPomodoroSessionPatchDto("task");

    String pomodoroDtoJson = objectMapper.writeValueAsString(pomodoroSessionPatchDto);

    mockMvc
        .perform(
            patch("/api/v1/pomodoro-sessions/" + pomodoroId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(pomodoroDtoJson))
        .andExpect(status().isOk())
        .andDo(print());

    verify(pomodoroSessionService)
        .updatePomodoroSession(eq(pomodoroId), eq(pomodoroSessionPatchDto.sessionType()));
  }
}
