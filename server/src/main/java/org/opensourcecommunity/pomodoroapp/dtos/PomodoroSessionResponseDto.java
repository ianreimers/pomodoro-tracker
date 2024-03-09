
package org.opensourcecommunity.pomodoroapp.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;

import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;

public record PomodoroSessionResponseDto(
		Long id,
		UUID tempUuid,
		Integer studyDuration,
		Integer breakDuration,
		Integer sessionStudyTime,
		Integer sessionShortBreakTime,
		Integer sessionLongBreakTime,
		ZonedDateTime sessionStartTime,
		ZonedDateTime sessionUpdateTime,
		BreakTypeEnum breakType) {

}
