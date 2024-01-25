package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.requestDTO.ScheduleldRequest;
import com.school.sba.responseDTO.ScheduleldResponse;
import com.school.sba.service.ScheduleldService;
import com.school.sba.util.ResponseStructure;

@RestController
public class ScheduleldController {
	@Autowired
	private ScheduleldService scheduleldService;
	@PostMapping("/schools/{schoolId}/schedules")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<ScheduleldResponse>> adminCreatesSchedule(@PathVariable int schoolId, @RequestBody ScheduleldRequest scheduleldRequest) {
		return scheduleldService.adminCreatesSchedule(schoolId, scheduleldRequest);
	}
	@GetMapping("/schools/{schoolId}/schedules")
	public ResponseEntity<ResponseStructure<ScheduleldResponse>> findScheduleBySchoolId(@PathVariable int schoolId) {
		return scheduleldService.findScheduleBySchoolId(schoolId);
	}
	@PutMapping("schedules/{scheduleldId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<ScheduleldResponse>> updateScheduleById(@PathVariable int scheduleldId, @RequestBody ScheduleldRequest scheduleldRequest) {
		return scheduleldService.updateScheduleById(scheduleldId, scheduleldRequest);
	}
}
