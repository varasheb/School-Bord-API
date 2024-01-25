package com.school.sba.service;

import org.springframework.http.ResponseEntity;


import com.school.sba.requestDTO.UserRequest;
import com.school.sba.responseDTO.UserResponce;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

public interface UserService {

	ResponseEntity<ResponseStructure<UserResponce>> saveUser(UserRequest userRequest);

	ResponseEntity<ResponseStructure<UserResponce>> fetchById(int userId);

	ResponseEntity<ResponseStructure<UserResponce>> deleteById(int userId);

	ResponseEntity<ResponseStructure<UserResponce>> addSubjectToTheTeacher(int subjectId, int userId);

	ResponseEntity<ResponseStructure<UserResponce>> updateUser(int userId, UserRequest userrequest);

	ResponseEntity<ResponseStructure<UserResponce>> saveUserAdmin(@Valid UserRequest userRequest);

}
