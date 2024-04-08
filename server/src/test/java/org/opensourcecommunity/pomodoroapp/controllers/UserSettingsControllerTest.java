package org.opensourcecommunity.pomodoroapp.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.config.SecurityConfiguration;
import org.opensourcecommunity.pomodoroapp.dtos.UserSettingsDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.opensourcecommunity.pomodoroapp.services.UserService;
import org.opensourcecommunity.pomodoroapp.services.UserSettingsService;
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

@WebMvcTest(UserSettingsController.class)
@Import(SecurityConfiguration.class)
public class UserSettingsControllerTest {

  @MockBean private JwtService jwtService;
  @MockBean AuthenticationProvider authenticationProvider;
  @MockBean private UserSettingsService userSettingsService;
  @MockBean private UserService userService;

  @MockBean private Authentication auth;

  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @BeforeEach
  void setUp() {
    User user = TestDataFactory.createUser();
    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            user, null, AuthorityUtils.createAuthorityList("ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    when(auth.getPrincipal()).thenReturn(user);
  }

  @Test
  void getUserSettings_WithAuthUser_ReturnUserSettingsDto() throws Exception {
    User user = TestDataFactory.createUser();
    UserSettings userSettings = TestDataFactory.createUserSettings(1L, user);
    UserSettingsDto userSettingsDto = TestDataFactory.createUserSettingsDto(userSettings);

    when(userSettingsService.getUserSettingsByUser(any(User.class))).thenReturn(userSettingsDto);

    mockMvc
        .perform(get("/api/v1/settings"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.taskSeconds").value(userSettings.getTaskSeconds()))
        .andExpect(jsonPath("$.shortBreakSeconds").value(userSettings.getShortBreakSeconds()))
        .andExpect(jsonPath("$.longBreakSeconds").value(userSettings.getLongBreakSeconds()))
        .andExpect(jsonPath("$.pomodoroInterval").value(userSettings.getPomodoroInterval()))
        .andExpect(jsonPath("$.sound").value(userSettings.getSound()))
        .andDo(print());

    verify(userSettingsService).getUserSettingsByUser(user);
  }

  @Test
  void updateUserSettings_WithUserSettingsDtoAndAuthUser_ReturnUserSettingsDto() throws Exception {
    User user = TestDataFactory.createUser();
    UserSettings userSettings = TestDataFactory.createUserSettings(1L, user);
    UserSettingsDto userSettingsDto = TestDataFactory.createUserSettingsDto(userSettings);
    UserSettings updatedUserSettings = TestDataFactory.createUserSettings(1L, user);
    updatedUserSettings.setSound("congas");
    updatedUserSettings.setLongBreakSeconds(2000);
    UserSettingsDto updatedUserSettingsDto =
        TestDataFactory.createUserSettingsDto(updatedUserSettings);

    when(userSettingsService.updateUserSettings(any(User.class), any(UserSettingsDto.class)))
        .thenReturn(updatedUserSettingsDto);

    String objectJson = objectMapper.writeValueAsString(updatedUserSettingsDto);

    mockMvc
        .perform(
            put("/api/v1/settings").content(objectJson).contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.taskSeconds").value(userSettings.getTaskSeconds()))
        .andExpect(jsonPath("$.shortBreakSeconds").value(userSettings.getShortBreakSeconds()))
        .andExpect(jsonPath("$.longBreakSeconds").value(2000))
        .andExpect(jsonPath("$.pomodoroInterval").value(userSettings.getPomodoroInterval()))
        .andExpect(jsonPath("$.sound").value("congas"))
        .andDo(print());

    verify(userSettingsService).updateUserSettings(user, updatedUserSettingsDto);
  }
}
