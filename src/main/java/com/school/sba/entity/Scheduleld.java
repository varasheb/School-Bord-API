package com.school.sba.entity;

import java.time.Duration;
import java.time.LocalTime;

import org.springframework.stereotype.Component;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Scheduleld {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int scheduleld;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerDay;
	private Duration classHourLengthInMin;
	private LocalTime breakTime;
	private Duration breakLengthInMin;
	private LocalTime lunchTime;
	private Duration lunchLengthInMin;
	@OneToOne(mappedBy = "scheduleld")
	private School school;
}
