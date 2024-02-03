package com.school.sba.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.requestDTO.ClassHourDTO;
import com.school.sba.requestDTO.ExelRequestDTO;
import com.school.sba.responseDTO.ClassHourResponse;
import com.school.sba.util.ResponseStructure;

public interface ClassHourService {

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> createClassHoursForWeek(int programId);
	
	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourDTO> classHourDT0list);

	ResponseEntity<ResponseStructure<String>> deleteAll();

	ResponseEntity<ResponseStructure<List<ClassHourResponse>>> repeatClassHour();

	ResponseEntity<ResponseStructure<String>> updateClassHourInExel(int programId,
			ExelRequestDTO exelRequestDto);

}
