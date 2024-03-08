package org.opensourcecommunity.pomodoroapp.repositories;

import java.util.List;

import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u")
	List<User> findAllUsers();

	User findByUsername(String username);

}
