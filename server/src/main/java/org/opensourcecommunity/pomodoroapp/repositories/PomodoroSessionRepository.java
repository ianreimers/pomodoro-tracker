package org.opensourcecommunity.pomodoroapp.repositories;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.models.PomodoroSession;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.Tuple;

public interface PomodoroSessionRepository extends JpaRepository<PomodoroSession, Long> {

	public List<PomodoroSession> findAllByUserId(Long userId);

	public List<PomodoroSession> findAllByUser(User user);

	public User findUserByUserId(Long userId);

	@Query(value = "SELECT " +
			"COALESCE(SUM(CASE WHEN ps.task_duration = ps.session_task_seconds THEN 1 ELSE 0 END), 0) AS totalTasks, "
			+
			"COALESCE(SUM(ps.task_duration), 0) AS totalTaskSeconds, " +
			"COALESCE(SUM(CASE ps.break_type " +
			"   WHEN 'SHORT' THEN " +
			"       CASE " +
			"           WHEN ps.task_duration = ps.session_task_seconds AND ps.session_short_break_seconds = ps.break_duration THEN 1 "
			+
			"           ELSE 0 " +
			"       END " +
			"   WHEN 'LONG' THEN " +
			"       CASE " +
			"           WHEN ps.task_duration = ps.session_task_seconds AND ps.session_long_break_seconds = ps.break_duration THEN 1 "
			+
			"           ELSE 0 " +
			"       END " +
			"END), 0) AS totalPomodoros, " +
			"COALESCE(SUM(ps.task_duration + ps.break_duration), 0) AS totalSeconds " +
			"FROM pomodoro_sessions ps " +
			"WHERE ps.user_id = :userId AND DATE(ps.session_start_time) = CURRENT_DATE;", nativeQuery = true)
	public Tuple findTodayTotal(Long userId);

	@Query(value = "SELECT "
			+ "COALESCE(SUM(CASE WHEN ps.task_duration = ps.session_task_seconds THEN 1 ELSE 0 END), 0) AS totalTasks, "
			+ "COALESCE(SUM(ps.task_duration), 0) AS totalTaskSeconds, "
			+ "COALESCE(SUM(CASE ps.break_type "
			+ "   WHEN 'SHORT' THEN "
			+ "       CASE "
			+ "           WHEN ps.task_duration = ps.session_task_seconds AND ps.session_short_break_seconds = ps.break_duration THEN 1 "
			+ "           ELSE 0 "
			+ "       END "
			+ "   WHEN 'LONG' THEN "
			+ "       CASE "
			+ "           WHEN ps.task_duration = ps.session_task_seconds AND ps.session_long_break_seconds = ps.break_duration THEN 1 "
			+ "           ELSE 0 "
			+ "       END "
			+ "   END), 0) AS totalPomodoros, "
			+ "COALESCE(SUM(ps.task_duration + ps.break_duration), 0) AS totalSeconds "
			+ "FROM pomodoro_sessions ps "
			+ "WHERE ps.user_id = :userId", nativeQuery = true)
	public Tuple findAllTimeTotals(Long userId);

	@Query(value = "SELECT " +
			"  TO_CHAR(session_start_time, 'Day') AS dayOfTheWeek, " +
			"  SUM(task_duration) AS totalTaskSeconds, " +
			"  COALESCE(SUM(CASE break_type WHEN 'SHORT' THEN break_duration END), 0) AS totalShortBreakSeconds, "
			+
			"  COALESCE(SUM(CASE break_type WHEN 'LONG' THEN break_duration END), 0) AS totalLongBreakSeconds "
			+
			"FROM " +
			"  pomodoro_sessions " +
			"WHERE " +
			"  session_start_time >= date_trunc('week', CURRENT_TIMESTAMP) " +
			"  AND session_start_time < date_trunc('week', CURRENT_TIMESTAMP) + INTERVAL '1 week' " +
			"  AND user_id = :userId " +
			"GROUP BY " +
			"  TO_CHAR(session_start_time, 'Day')", nativeQuery = true)
	public List<Tuple> findCurrentWeekAnalytics(Long userId);

}
