package org.opensourcecommunity.pomodoroapp.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;

import jakarta.validation.constraints.NotNull;

public record PomodoroSessionDto(
		@NotNull UUID tempUuid,
		@NotNull Integer studyDuration,
		@NotNull Integer breakDuration,
		@NotNull Integer sessionStudyTime,
		@NotNull Integer sessionShortBreakTime,
		@NotNull Integer sessionLongBreakTime,
		@NotNull ZonedDateTime sessionStartTime,
		@NotNull ZonedDateTime sessionUpdateTime,
		@NotNull BreakTypeEnum breakType) {
}
