package org.opensourcecommunity.pomodoroapp.dtos;

public record PomodoroSessionAllTimeDto(
		Long totalTasks,
		Long totalTaskSeconds,
		Long totalPomodoros,
		Long totalSeconds) {
}
