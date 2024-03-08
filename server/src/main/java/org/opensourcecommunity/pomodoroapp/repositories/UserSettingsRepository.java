package org.opensourcecommunity.pomodoroapp.repositories;

import org.opensourcecommunity.pomodoroapp.models.User;
import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

	UserSettings findByUser(User user);

	UserSettings findByUserId(Long id);

	// Manual update of userSettings rather than executing 3 round trips to the
	// database
	//
	// @Modifying
	// @Transactional
	// @Query("UPDATE UserSettings us SET us.studyTime = :studyTime,
	// us.shortBreakTime = :shortBreakTime, us.longBreakTime = :longBreakTime WHERE
	// us.user.id = :userId")
	// int updateUserSettingsByUserId(Long userId, Integer studyTime, Integer
	// shortBreakTime,
	// Integer longBreakTime);

}
