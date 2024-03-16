package org.opensourcecommunity.pomodoroapp.repositories;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.dtos.PomodoroSessionDto;
import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {

	public List<PomodoroSession> findAllByUserId(Long userId);

	public List<PomodoroSession> findAllByUser(User user);

	public User findUserByUserId(Long userId);

}
