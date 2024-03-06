package org.opensourcecommunity.pomodoroapp.repositories;

import org.opensourcecommunity.pomodoroapp.models.UserSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSettingsRepository extends JpaRepository<UserSettings, Long> {

}
