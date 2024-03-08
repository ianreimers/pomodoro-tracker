package org.opensourcecommunity.pomodoroapp.models;

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
	@Column(unique = true)
	private UUID tempId;
	private Integer studyDuration;
	private Integer breakDuration;
	private Integer setStudyTime;
	private Integer setShortBreakTime;
	private Integer setLongBreakTime;
	@Enumerated(EnumType.STRING)
	private BreakTypeEnum breakType;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	@OneToMany(mappedBy = "pomodoroSession")
	@JsonManagedReference
	private List<Pause> pauses;
}
