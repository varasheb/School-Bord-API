package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestDTO.AcademicProgramRequest;
import com.school.sba.responseDTO.AcademicProgramResponse;
import com.school.sba.responseDTO.UserResponce;
import com.school.sba.util.ResponseStructure;

public interface AcademicProgramService {

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> addsAcademicProgram(int schoolId,
			AcademicProgramRequest academicProgramRequest);

	ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> fetchAllAcademicProgram(int schoolId);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> assignTeachersStudentsToAcademicProgram(int programId,
			int userId);

	ResponseEntity<ResponseStructure<List<UserResponce>>> fetchUsersByRoleInAcademicProgram(int programId, String role);

	ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteById(int academicProgramId);
	
	void permantDelete();
}
