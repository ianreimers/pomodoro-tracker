package org.opensourcecommunity.pomodoroapp.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

public class UserTest {

  @Test
  void userDetailsImplementation_ReturnsCorrectAuthorities() {

    User user =
        User.builder()
            .username("testUser")
            .email("test@example.com")
            .password("password")
            .role(Role.USER)
            .build();

    Collection<? extends GrantedAuthority> authorities = user.getAuthorities();

    assertEquals(1, authorities.size());
    assertEquals("USER", authorities.iterator().next().getAuthority());
  }
}
