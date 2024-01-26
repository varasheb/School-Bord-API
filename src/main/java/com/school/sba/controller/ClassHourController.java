package com.school.sba.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.responseDTO.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@RestController
public class ClassHourController {
	@Autowired
	private ClassHourService classhourService;
	
	@PostMapping("/academic-program/{programId}/class-hours")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> createsClassHour(@PathVariable int programId) {
		return classhourService.createClassHoursForWeek(programId);
	}
}
