package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestDTO.SubjectRequest;
import com.school.sba.responseDTO.SubjectResponse;
import com.school.sba.util.ResponseStructure;

public interface SubjectService {

	ResponseEntity<ResponseStructure<List<SubjectResponse>>> addSubjectToAcademicProgram(int programId,
			List<SubjectRequest> subjectRequest);

	ResponseEntity<ResponseStructure<List<SubjectResponse>>> fetchAllSubjects();
}
