package org.opensourcecommunity.pomodoroapp.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "pauses")
@AllArgsConstructor
@SuperBuilder
public class Pause extends BaseEntity {

	@ManyToOne
	@JoinColumn(name = "pomodoro_session_id")
	@JsonBackReference
	private PomodoroSession pomodoroSession;

}
