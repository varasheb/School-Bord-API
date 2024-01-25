package com.school.sba.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.school.sba.entity.School;
import com.school.sba.requestDTO.SchoolRequest;
import com.school.sba.responseDTO.SchoolResponce;
import com.school.sba.util.ResponseStructure;

public interface SchoolService {
	
	public ResponseEntity<ResponseStructure<SchoolResponce>> adminCreatesSchool(int userId, SchoolRequest schoolRequest);
	
}