package org.opensourcecommunity.pomodoroapp.models;

import java.time.ZonedDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@AllArgsConstructor
@SuperBuilder
public class User extends BaseEntity {
	@Column(unique = true, nullable = false)
	private String username;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Column(updatable = false, nullable = false)
	@CreationTimestamp
	private ZonedDateTime createdAt;
	@Column(nullable = false)
	@UpdateTimestamp
	private ZonedDateTime updatedAt;

	@OneToOne(mappedBy = "user")
	private UserSettings userSettings;

	@OneToMany(mappedBy = "user")
	private List<PomodoroSession> pomodoroSessions;

}
