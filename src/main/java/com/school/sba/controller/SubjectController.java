package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestDTO.SubjectRequest;
import com.school.sba.responseDTO.SubjectResponse;
import com.school.sba.service.SubjectService;
import com.school.sba.util.ResponseStructure;

@RestController
public class SubjectController {
	@Autowired
	private SubjectService subjectService;
	@PostMapping("/academicPrograms/{programId}/subjects")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> addSubjectToAcademicProgram(@PathVariable int programId, @RequestBody List<SubjectRequest> subjectRequest){
		return subjectService.addSubjectToAcademicProgram(programId, subjectRequest);
	}
	@GetMapping("/subjects")
	public ResponseEntity<ResponseStructure<List<SubjectResponse>>> fetchAllSubjects(){
		return subjectService.fetchAllSubjects();
	}
}
