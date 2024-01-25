package com.school.sba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.school.sba.entity.User;
import com.school.sba.requestDTO.UserRequest;
import com.school.sba.responseDTO.UserResponce;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@PostMapping("users/register")
	public ResponseEntity<ResponseStructure<UserResponce>> saveUserAdmin(@RequestBody @Valid UserRequest userRequest){
		return userService.saveUserAdmin(userRequest);
	}
	@PostMapping("users")
	public ResponseEntity<ResponseStructure<UserResponce>> saveUser(@RequestBody @Valid UserRequest userRequest){
		return userService.saveUser(userRequest);
	}
	@GetMapping("users/{userId}")
	public ResponseEntity<ResponseStructure<UserResponce>> fetchById (@PathVariable int userId){
		return userService.fetchById(userId);
	}
	@DeleteMapping("users/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponce>> deleteById(@PathVariable int userId){
		return userService.deleteById(userId);
	}
	@PutMapping("/user/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public  ResponseEntity<ResponseStructure<UserResponce>> updateUser(@PathVariable int userId,@RequestBody @Valid UserRequest userRequest){
		return userService.updateUser(userId,userRequest );
	} 
	
	@PutMapping("users/subjects/{subjectId}/users/{userId}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public ResponseEntity<ResponseStructure<UserResponce>> addSubjectToTheTeacher(@PathVariable int subjectId, @PathVariable int userId){
		return userService.addSubjectToTheTeacher(subjectId, userId);
	}
}
