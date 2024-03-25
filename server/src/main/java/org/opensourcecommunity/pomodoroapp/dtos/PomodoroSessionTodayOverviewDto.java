
package org.opensourcecommunity.pomodoroapp.dtos;

public record PomodoroSessionTodayOverviewDto(
		Long totalTasks,
		Long totalTaskSeconds,
		Long totalPomodoros,
		Long totalSeconds) {
}
