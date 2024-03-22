package org.opensourcecommunity.pomodoroapp.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;

import jakarta.validation.constraints.NotNull;

public record PomodoroSessionDto(
		@NotNull UUID tempUuid,
		@NotNull Integer taskDuration,
		@NotNull Integer breakDuration,
		@NotNull Integer sessionTaskSeconds,
		@NotNull Integer sessionShortBreakSeconds,
		@NotNull Integer sessionLongBreakSeconds,
		@NotNull ZonedDateTime sessionStartTime,
		@NotNull ZonedDateTime sessionUpdateTime,
		@NotNull BreakTypeEnum breakType) {
}
