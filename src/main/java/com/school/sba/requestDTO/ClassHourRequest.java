package com.school.sba.requestDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;

import com.school.sba.Enum.ClassStatus;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassHourRequest {
	private LocalDateTime beginsAt;
	private LocalDateTime endsAt;
	private String roomNo;
	private ClassStatus classStatus;
}
