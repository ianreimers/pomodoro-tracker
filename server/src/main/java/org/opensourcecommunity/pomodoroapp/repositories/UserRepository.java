package org.opensourcecommunity.pomodoroapp.repositories;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
