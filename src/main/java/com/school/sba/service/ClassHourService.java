package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.responseDTO.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> createClassHoursForWeek(int programId);

}