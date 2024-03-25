package org.opensourcecommunity.pomodoroapp.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionTodayOverviewDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionAllTimeDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionResponseDto;
import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionWeeklyDto;
import org.opensourcecommunity.pomodoroapp.mappers.PomodoroSessionMapper;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.repositories.PomodoroSessionRepository;
import org.opensourcecommunity.pomodoroapp.repositories.UserRepository;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Tuple;

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

	public PomodoroSessionResponseDto createPomodoroSession(User user, PomodoroSessionDto dto) {
		// User user = userRepository.findByUsername(username).get();
		PomodoroSession pomodoro = pomodoroSessionMapper.pomodoroSessionDtoToPomodoroSession(user, dto);
		PomodoroSession savedPomodoro = pomodoroSessionRepository.save(pomodoro);

		return pomodoroSessionMapper.pomodoroSessionToPomodoroResponseDto(savedPomodoro);
	}

	public void updatePomodoroSession(Long pomodoroId, String sessionType) {
		PomodoroSession pomodoro = pomodoroSessionRepository.findById(pomodoroId).orElseThrow(
				() -> new EntityNotFoundException("Pomodoro not found with id " + pomodoroId));

		if ("task".equalsIgnoreCase(sessionType)) {
			pomodoro.setTaskDuration(pomodoro.getTaskDuration() + 60);
		} else if ("break".equalsIgnoreCase(sessionType)) {
			pomodoro.setBreakDuration(pomodoro.getBreakDuration() + 60);
		} else {
			throw new IllegalArgumentException("Invalid session type: " + sessionType);
		}

		pomodoroSessionRepository.save(pomodoro);
	}

	public PomodoroSessionTodayOverviewDto getTodayTotals(User user) {
		Tuple todayTotalsTuple = pomodoroSessionRepository.findTodayTotal(user.getId());

		PomodoroSessionTodayOverviewDto dto = new PomodoroSessionTodayOverviewDto(
				todayTotalsTuple.get(0, Long.class),
				todayTotalsTuple.get(1, Long.class),
				todayTotalsTuple.get(2, Long.class),
				todayTotalsTuple.get(3, Long.class));

		return dto;
	}

	public PomodoroSessionAllTimeDto getAllTimeTotals(User user) {
		Tuple allTimeTotalsTuple = pomodoroSessionRepository.findAllTimeTotals(user.getId());

		PomodoroSessionAllTimeDto dto = new PomodoroSessionAllTimeDto(
				allTimeTotalsTuple.get(0, Long.class),
				allTimeTotalsTuple.get(1, Long.class),
				allTimeTotalsTuple.get(2, Long.class),
				allTimeTotalsTuple.get(3, Long.class));

		return dto;
	}

	public List<PomodoroSessionWeeklyDto> getCurrentWeeklyAnalytics(User user) {
		List<Tuple> currentWeekTuple = pomodoroSessionRepository.findCurrentWeekAnalytics(user.getId());

		List<PomodoroSessionWeeklyDto> dto = currentWeekTuple.stream()
				.map(a -> new PomodoroSessionWeeklyDto(
						a.get(0, String.class),
						a.get(1, Long.class),
						a.get(2, Long.class),
						a.get(3, Long.class)))
				.collect(Collectors.toList());
		return dto;

	}

}
