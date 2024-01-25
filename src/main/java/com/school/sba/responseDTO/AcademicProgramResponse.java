package com.school.sba.responseDTO;

import java.time.LocalTime;
import java.util.List;

import com.school.sba.Enum.ProgramType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AcademicProgramResponse {
	private int programId;
	private String programName;
	private ProgramType programType;
	private LocalTime beginsAt;
	private LocalTime endsAt;
	private List<String> user;
}
