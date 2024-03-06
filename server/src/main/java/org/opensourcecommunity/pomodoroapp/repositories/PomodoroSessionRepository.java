package org.opensourcecommunity.pomodoroapp.repositories;

import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {

}
