package org.opensourcecommunity.pomodoroapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class UserSettings extends BaseEntity {
	@Column(nullable = false, columnDefinition = "INT DEFAULT 1500")
	@Builder.Default
	private Integer taskSeconds = 1500;
	@Column(nullable = false, columnDefinition = "INT DEFAULT 600")
	@Builder.Default
	private Integer shortBreakSeconds = 600;
	@Column(nullable = false, columnDefinition = "INT DEFAULT 1800")
	@Builder.Default
	private Integer longBreakSeconds = 1800;
	@Column(nullable = false, columnDefinition = "INT DEFAULT 4")
	@Builder.Default
	private Integer pomodoroInterval = 4;
	@Column(nullable = false, columnDefinition = "TEXT DEFAULT 'bells'")
	@Builder.Default
	private String sound = "bells";

	@OneToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;
}
