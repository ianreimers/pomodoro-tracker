package org.opensourcecommunity.pomodoroapp.models;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "pomodoro_sessions")
@AllArgsConstructor
@SuperBuilder
public class PomodoroSession extends BaseEntity {
	@NotNull
	private UUID tempUuid;
	private Integer taskDuration;
	private Integer breakDuration;
	@Column(nullable = false)
	private Integer sessionTaskSeconds;
	@Column(nullable = false)
	private Integer sessionShortBreakSeconds;
	@Column(nullable = false)
	private Integer sessionLongBreakSeconds;
	@Column(nullable = false)
	private ZonedDateTime sessionStartTime;
	@Column(nullable = false)
	private ZonedDateTime sessionUpdateTime;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private BreakTypeEnum breakType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@OneToMany(mappedBy = "pomodoroSession")
	@JsonManagedReference
	private List<Pause> pauses;
}
