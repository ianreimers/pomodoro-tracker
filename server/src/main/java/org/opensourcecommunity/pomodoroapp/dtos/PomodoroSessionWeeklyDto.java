
package org.opensourcecommunity.pomodoroapp.dtos;

public record PomodoroSessionWeeklyDto(
		String dayOfTheWeek,
		Long totalTaskSeconds,
		Long totalShortBreakSeconds,
		Long totalLongBreakSeconds) {
}
