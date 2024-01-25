package com.school.sba.requestDTO;

import java.time.LocalTime;

import com.school.sba.Enum.ProgramType;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicProgramRequest {
	private String programName;
	private ProgramType programType;
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime beginsAt;
	@Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Invalid time format. Please use HH:mm")
	private LocalTime endsAt;
}
