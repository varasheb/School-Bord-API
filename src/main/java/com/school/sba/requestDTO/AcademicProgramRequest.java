package com.school.sba.requestDTO;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.school.sba.Enum.ProgramType;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcademicProgramRequest {
	private String programName;
	private ProgramType programType;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Please use yyyy-MM-dd")
	private LocalDate beginsAt;
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	@Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Invalid date format. Please use yyyy-MM-dd")
	private LocalDate endsAt;
}
