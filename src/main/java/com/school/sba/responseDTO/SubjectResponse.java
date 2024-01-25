package com.school.sba.responseDTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SubjectResponse {
	private int subjectId;
	private String subjectName;
}
