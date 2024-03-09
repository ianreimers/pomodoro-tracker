package org.opensourcecommunity.pomodoroapp.mappers;

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
		pomodoro.setStudyDuration(dto.studyDuration());
		pomodoro.setBreakDuration(dto.breakDuration());
		pomodoro.setSessionStudyTime(dto.sessionStudyTime());
		pomodoro.setSessionShortBreakTime(dto.sessionShortBreakTime());
		pomodoro.setSessionLongBreakTime(dto.sessionLongBreakTime());
		pomodoro.setSessionStartTime(dto.sessionStartTime());
		pomodoro.setSessionUpdateTime(dto.sessionUpdateTime());
		pomodoro.setBreakType(dto.breakType());

		return pomodoro;
	}

	public PomodoroSessionResponseDto pomodoroSessionToPomodoroResponseDto(PomodoroSession pomodoroSession) {
		return new PomodoroSessionResponseDto(
				pomodoroSession.getId(),
				pomodoroSession.getTempUuid(),
				pomodoroSession.getStudyDuration(),
				pomodoroSession.getBreakDuration(),
				pomodoroSession.getSessionStudyTime(),
				pomodoroSession.getSessionShortBreakTime(),
				pomodoroSession.getSessionLongBreakTime(),
				pomodoroSession.getSessionStartTime(),
				pomodoroSession.getSessionUpdateTime(),
				pomodoroSession.getBreakType());

	}

}
