package org.opensourcecommunity.pomodoroapp.repositories;

import org.opensourcecommunity.pomodoroapp.models.Pause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PauseRepository extends JpaRepository<Pause, Long> {

}
