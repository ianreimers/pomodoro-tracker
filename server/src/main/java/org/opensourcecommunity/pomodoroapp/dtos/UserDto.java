
package org.opensourcecommunity.pomodoroapp.dtos;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserDto(
		@NotBlank String username,
		@Email @NotBlank String email,
		@Length(min = 7) @NotBlank String password) {
}
