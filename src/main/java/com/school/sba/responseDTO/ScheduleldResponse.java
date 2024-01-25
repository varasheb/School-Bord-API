package com.school.sba.responseDTO;

import java.time.Duration;
import java.time.LocalTime;

import com.school.sba.entity.School;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ScheduleldResponse {
	private int scheduleld;
	private LocalTime opensAt;
	private LocalTime closesAt;
	private int classHoursPerDay;
	private int classHourLengthInMin;
	private LocalTime breakTime;
	private int breakLengthInMin;
	private LocalTime lunchTime;
	private int lunchLengthInMin;
}
