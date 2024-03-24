package org.opensourcecommunity.pomodoroapp.mappers;

import java.util.List;
import java.util.stream.Collectors;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.stereotype.Service;

@Service
public class PomodoroSessionMapper {

	public PomodoroSession pomodoroSessionDtoToPomodoroSession(User user, PomodoroSessionDto dto) {
		PomodoroSession pomodoro = new PomodoroSession();
		pomodoro.setUser(user);
		pomodoro.setTempUuid(dto.tempUuid());
		pomodoro.setTaskDuration(dto.taskDuration());
		pomodoro.setBreakDuration(dto.breakDuration());
		pomodoro.setSessionTaskSeconds(dto.sessionTaskSeconds());
		pomodoro.setSessionShortBreakSeconds(dto.sessionShortBreakSeconds());
		pomodoro.setSessionLongBreakSeconds(dto.sessionLongBreakSeconds());
		pomodoro.setSessionStartTime(dto.sessionStartTime());
		pomodoro.setSessionUpdateTime(dto.sessionUpdateTime());
		pomodoro.setBreakType(dto.breakType());

		return pomodoro;
	}

	public PomodoroSessionResponseDto pomodoroSessionToPomodoroResponseDto(PomodoroSession pomodoroSession) {
		return new PomodoroSessionResponseDto(
				pomodoroSession.getId(),
				pomodoroSession.getTempUuid(),
				pomodoroSession.getTaskDuration(),
				pomodoroSession.getBreakDuration(),
				pomodoroSession.getSessionTaskSeconds(),
				pomodoroSession.getSessionShortBreakSeconds(),
				pomodoroSession.getSessionLongBreakSeconds(),
				pomodoroSession.getSessionStartTime(),
				pomodoroSession.getSessionUpdateTime(),
				pomodoroSession.getBreakType());

	}

	public List<PomodoroSessionResponseDto> pomodoroSessionsToPomodoroResponseDtos(
			List<PomodoroSession> pomodoros) {
		return pomodoros.stream()
				.map(this::pomodoroSessionToPomodoroResponseDto)
				.collect(Collectors.toList());
	}

}
