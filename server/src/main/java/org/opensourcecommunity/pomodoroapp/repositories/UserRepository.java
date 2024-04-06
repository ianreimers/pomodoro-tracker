package org.opensourcecommunity.pomodoroapp.repositories;

import java.util.Optional;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUsername(String username);
}
