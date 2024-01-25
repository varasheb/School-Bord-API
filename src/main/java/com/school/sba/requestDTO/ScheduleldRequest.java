package com.school.sba.requestDTO;

import java.time.LocalTime;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleldRequest {
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime opensAt;
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime closesAt;
	private int classHoursPerDay;
	private int classHourLengthInMin;
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime breakTime;
	private int breakLengthInMin;
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime lunchTime;
	private int lunchLengthInMin;
}
