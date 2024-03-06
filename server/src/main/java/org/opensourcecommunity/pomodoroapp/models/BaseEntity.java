package org.opensourcecommunity.pomodoroapp.models;

import java.time.ZonedDateTime;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@MappedSuperclass
public class BaseEntity {
	@Id
	@GeneratedValue
	private Long id;

	private ZonedDateTime createdAt;
	private ZonedDateTime lastModifiedAt;
	private String createdBy;
	private String lastModifiedBy;

}
