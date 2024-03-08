
package org.opensourcecommunity.pomodoroapp.dtos;

import org.opensourcecommunity.pomodoroapp.models.UserSettings;

public record UserResponseDto(
		Long id,
		String username,
		String email,
		UserSettings userSettings) {

}
