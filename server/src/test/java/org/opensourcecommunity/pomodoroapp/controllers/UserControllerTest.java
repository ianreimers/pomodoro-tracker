package org.opensourcecommunity.pomodoroapp.controllers;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.opensourcecommunity.pomodoroapp.config.JwtService;
import org.opensourcecommunity.pomodoroapp.config.SecurityConfiguration;
import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.exceptions.UserNotFoundException;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
// @Import({SecurityConfiguration.class, ApplicationConfig.class})
@Import(SecurityConfiguration.class)
@WithMockUser
public class UserControllerTest {
  @MockBean private AuthenticationProvider authenticationProvider;

  @MockBean private JwtService jwtService;
  @MockBean private UserService userService;
  @MockBean private UserSettingsService userSettingsService;

  @Autowired private ObjectMapper objectMapper;
  @Autowired private MockMvc mockMvc;

  @Test
  void getAllUsersTest_ReturnUserResponseDtos() throws Exception {
    User user = TestDataFactory.createUser();
    UserSettings userSettings = TestDataFactory.createUserSettings(user.getId(), null);
    UserResponseDto userResponseDto = TestDataFactory.createUserResponseDto(user, userSettings);
    List<UserResponseDto> userResponseDtos = Arrays.asList(userResponseDto);

    when(userService.getAllUsers()).thenReturn(userResponseDtos);

    mockMvc
        .perform(get("/api/v1/users").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$[0].id").value(userResponseDto.id()))
        .andExpect(jsonPath("$[0].username").value(userResponseDto.username()))
        .andExpect(jsonPath("$[0].email").value(userResponseDto.email()))
        .andExpect(jsonPath("$[0].userSettings").value(userResponseDto.userSettings()))
        .andDo(print());

    // Verify that the service method was called once
    verify(userService).getAllUsers();
  }

  @Test
  void getUserById_WithValidId_ReturnUserResponseDto() throws Exception {
    User user = TestDataFactory.createUser();
    UserResponseDto userResponseDto = TestDataFactory.createUserResponseDto(user, null);

    when(userService.getUserResponseDtoById(any(Long.class))).thenReturn(userResponseDto);

    mockMvc
        .perform(get("/api/v1/users/1").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("id").value(userResponseDto.id()))
        .andExpect(jsonPath("username").value(userResponseDto.username()))
        .andExpect(jsonPath("email").value(userResponseDto.email()))
        .andExpect(jsonPath("userSettings").value(userResponseDto.userSettings()))
        .andDo(print());

    // Verify that the service method was called once
    verify(userService).getUserResponseDtoById(user.getId());
  }

  @Test
  void getUserById_WithInvalidId_ThrowUserNotFoundException() throws Exception {
    Long invalidUserId = 1L;

    when(userService.getUserResponseDtoById(invalidUserId))
        .thenThrow(new UserNotFoundException("User does not exist with id " + invalidUserId));

    mockMvc
        .perform(get("/api/v1/users/" + invalidUserId))
        .andExpect(status().isNotFound())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.message").value("User does not exist with id " + invalidUserId))
        .andDo(print());

    verify(userService).getUserResponseDtoById(invalidUserId);
  }

  @Test
  void createUser_WithValidUserDto_ReturnUserResponseDto() throws Exception {
    User user = TestDataFactory.createUser();
    UserSettings userSettings = TestDataFactory.createUserSettings(1L, user);
    UserDto userDto = TestDataFactory.createUserDto(user);
    UserResponseDto userResponseDto = TestDataFactory.createUserResponseDto(user);

    when(userService.createUser(any(UserDto.class))).thenReturn(user);
    when(userSettingsService.createUserSettings(user))
        .thenAnswer(
            i -> {
              User passedUser = i.getArgument(0, User.class);
              passedUser.setUserSettings(userSettings);
              return userSettings;
            });
    when(userService.convertUserToUserResponseDto(user)).thenReturn(userResponseDto);

    String userDtoJson = objectMapper.writeValueAsString(userDto);

    mockMvc
        .perform(post("/api/v1/users").content(userDtoJson).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.id").value(user.getId()))
        .andExpect(jsonPath("$.username").value(user.getUsername()))
        .andExpect(jsonPath("$.email").value(user.getEmail()))
        .andExpect(jsonPath("$.userSettings.id").value(user.getUserSettings().getId()))
        .andDo(print());

    verify(userService).createUser(any(UserDto.class));
    verify(userSettingsService).createUserSettings(any(User.class));
    verify(userService).convertUserToUserResponseDto(any(User.class));
  }

  @Test
  void deleteUser_WithValidId_ReturnNoContent() throws Exception {
    Long validId = 1L;

    doNothing().when(userService).deleteUserById(validId);

    mockMvc
        .perform(delete("/api/v1/users/" + validId))
        .andExpect(status().isNoContent())
        .andDo(print());

    verify(userService).deleteUserById(validId);
  }
}
