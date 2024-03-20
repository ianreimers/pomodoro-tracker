package org.opensourcecommunity.pomodoroapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserSettingsDto(
		@NotNull @Min(1) Integer taskSeconds,
		@NotNull @Min(1) Integer shortBreakSeconds,
		@NotNull @Min(2) Integer longBreakSeconds,
		@NotNull @Min(1) Integer pomodoroInterval) {
}
