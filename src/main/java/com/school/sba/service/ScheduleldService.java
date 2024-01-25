package com.school.sba.service;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestDTO.ScheduleldRequest;
import com.school.sba.responseDTO.ScheduleldResponse;
import com.school.sba.util.ResponseStructure;

public interface ScheduleldService {

	ResponseEntity<ResponseStructure<ScheduleldResponse>> adminCreatesSchedule(int schoolId,
			ScheduleldRequest scheduleldRequest);

	ResponseEntity<ResponseStructure<ScheduleldResponse>> findScheduleBySchoolId(int schoolId);

	ResponseEntity<ResponseStructure<ScheduleldResponse>> updateScheduleById(int scheduleldId,
			ScheduleldRequest scheduleldRequest);

}
