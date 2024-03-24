
package org.opensourcecommunity.pomodoroapp.dtos;

public record PomodoroSessionOverviewDto(
		Integer dailyTotal,
		Long totalTaskSeconds,
		Long totalCompletedTasks,
		Long totalCompletedPomodoros) {
}
