package org.opensourcecommunity.pomodoroapp.services;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.mappers.PomodoroSessionMapper;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.PomodoroSessionRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PomodoroSessionService {
	PomodoroSessionRepository pomodoroSessionRepository;
	PomodoroSessionMapper pomodoroSessionMapper;

	public PomodoroSessionService(PomodoroSessionRepository pomodoroSessionRepository,
			PomodoroSessionMapper pomodoroSessionMapper) {
		this.pomodoroSessionRepository = pomodoroSessionRepository;
		this.pomodoroSessionMapper = pomodoroSessionMapper;
	}

	public List<PomodoroSession> getPomodoroSessionsByUserId(Long userId) {
		return pomodoroSessionRepository.findAllByUserId(userId);
	}

	public List<PomodoroSession> getAllPomodoroSessions() {
		return pomodoroSessionRepository.findAll();
	}

	public PomodoroSessionResponseDto createPomodoroSession(User user, PomodoroSessionDto dto) {
		PomodoroSession pomodoro = pomodoroSessionMapper.pomodoroSessionDtoToPomodoroSession(user, dto);
		PomodoroSession savedPomodoro = pomodoroSessionRepository.save(pomodoro);

		return pomodoroSessionMapper.pomodoroSessionToPomodoroResponseDto(savedPomodoro);

	}

	public void updatePomodoroSession(Long userId, Long pomodoroId, String durationType) {
		PomodoroSession pomodoro = pomodoroSessionRepository.findById(pomodoroId).orElseThrow(
				() -> new EntityNotFoundException("Pomodoro not found with id " + pomodoroId));

		if ("study".equalsIgnoreCase(durationType)) {
			pomodoro.setStudyDuration(pomodoro.getStudyDuration() + 60);
		} else if ("break".equalsIgnoreCase(durationType)) {
			pomodoro.setBreakDuration(pomodoro.getBreakDuration() + 60);
		} else {
			throw new IllegalArgumentException("Invalid duration type: " + durationType);
		}

		pomodoroSessionRepository.save(pomodoro);
	}

}
