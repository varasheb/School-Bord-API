package com.school.sba.responseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolResponce {
	private int schoolId;
	private String schoolName;
	private int schoolContactNo;
	private String schoolEmailId;
	private String schoolAddress;
}
