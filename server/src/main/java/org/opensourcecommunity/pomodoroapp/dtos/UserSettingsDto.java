package org.opensourcecommunity.pomodoroapp.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UserSettingsDto(
		@NotNull @Min(1) Integer studyTime,
		@NotNull @Min(1) Integer shortBreakTime,
		@NotNull @Min(2) Integer longBreakTime) {
}
