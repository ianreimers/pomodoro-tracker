
package org.opensourcecommunity.pomodoroapp.mappers;

import org.opensourcecommunity.pomodoroapp.dtos.UserDto;
import org.opensourcecommunity.pomodoroapp.dtos.UserResponseDto;
import org.opensourcecommunity.pomodoroapp.models.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

	public UserResponseDto userToUserResponseDto(User user) {
		return new UserResponseDto(
				user.getId(),
				user.getUsername(),
				user.getEmail(),
				user.getUserSettings());
	}

	public UserDto userToUserDto(User user) {
		return new UserDto(
				user.getUsername(),
				user.getEmail(),
				user.getPassword());
	}

	public User userDtoToUser(UserDto dto) {
		User user = new User();
		user.setUsername(dto.username());
		user.setEmail(dto.email());
		user.setPassword(dto.password());

		return user;
	}

}
