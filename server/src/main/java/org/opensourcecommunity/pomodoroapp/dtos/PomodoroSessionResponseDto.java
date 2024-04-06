package org.opensourcecommunity.pomodoroapp.dtos;

import java.time.ZonedDateTime;
import java.util.UUID;
import org.opensourcecommunity.pomodoroapp.models.BreakTypeEnum;

public record PomodoroSessionResponseDto(
    Long id,
    UUID tempUuid,
    Integer taskDuration,
    Integer breakDuration,
    Integer sessionTaskSeconds,
    Integer sessionShortBreakSeconds,
    Integer sessionLongBreakSeconds,
    ZonedDateTime sessionStartTime,
    ZonedDateTime sessionUpdateTime,
    BreakTypeEnum breakType) {}
