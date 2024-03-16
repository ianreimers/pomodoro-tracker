package org.opensourcecommunity.pomodoroapp.services;

import java.util.List;
import java.util.stream.Collectors;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.mappers.PomodoroSessionMapper;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.PomodoroSessionRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PomodoroSessionService {
	PomodoroSessionRepository pomodoroSessionRepository;
	PomodoroSessionMapper pomodoroSessionMapper;
	UserRepository userRepository;

	public PomodoroSessionService(PomodoroSessionRepository pomodoroSessionRepository,
			PomodoroSessionMapper pomodoroSessionMapper, UserRepository userRepository) {
		this.pomodoroSessionRepository = pomodoroSessionRepository;
		this.pomodoroSessionMapper = pomodoroSessionMapper;
		this.userRepository = userRepository;
	}

	public List<PomodoroSessionResponseDto> getPomodoroSessionsByUser(String username) {
		User user = userRepository.findByUsername(username).get();

		List<PomodoroSession> pomodoros = pomodoroSessionRepository.findAllByUser(user);

		return pomodoroSessionMapper.pomodoroSessionsToPomodoroResponseDtos(pomodoros);
	}

	public PomodoroSessionResponseDto createPomodoroSession(String username, PomodoroSessionDto dto) {
		User user = userRepository.findByUsername(username).get();
		PomodoroSession pomodoro = pomodoroSessionMapper.pomodoroSessionDtoToPomodoroSession(user, dto);
		PomodoroSession savedPomodoro = pomodoroSessionRepository.save(pomodoro);

		return pomodoroSessionMapper.pomodoroSessionToPomodoroResponseDto(savedPomodoro);
	}

	public void updatePomodoroSession(Long pomodoroId, String durationType) {
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
