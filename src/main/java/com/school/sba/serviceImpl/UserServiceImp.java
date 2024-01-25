package com.school.sba.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.User;
import com.school.sba.exception.AdminFoundException;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestDTO.SchoolRequest;
import com.school.sba.requestDTO.UserRequest;
import com.school.sba.responseDTO.SchoolResponce;
import com.school.sba.responseDTO.UserResponce;
import com.school.sba.service.UserService;
import com.school.sba.util.ResponseStructure;

import jakarta.validation.Valid;

@Service
public class UserServiceImp implements UserService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private SubjectRepository subjectRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private PasswordEncoder encoded;

	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> saveUserAdmin(@Valid UserRequest userRequest) {
		if (!userRepository.existsByUserRole(UserRole.ADMIN)) {
			User user = userRepository.save(mapToUser(userRequest));
			ResponseStructure<UserResponce> responseStructure = new ResponseStructure<UserResponce>();
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("Saved Successfully!!!");
			responseStructure.setData(mapToResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponce>>(responseStructure, HttpStatus.CREATED);
		}else
			throw new AdminFoundException("Admin already exist");
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> saveUser(UserRequest userRequest) {
		if (userRepository.existsByUserRole(UserRole.ADMIN)) {
			Optional<User> opuser=userRepository.findByUserRole(UserRole.ADMIN);
			userRequest.setSchool(opuser.get().getSchool());
			User user = userRepository.save(mapToUser(userRequest));
			ResponseStructure<UserResponce> responseStructure = new ResponseStructure<UserResponce>();
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("Saved Successfully!!!");
			responseStructure.setData(mapToResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponce>>(responseStructure, HttpStatus.CREATED);
		}else
			throw new AdminFoundException("No admin exist");
		
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> fetchById(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("UserId does not exist!!!"));
		if (user.isDeleated() == true) {
			throw new UserNotFoundByIdException("UserId has already been deleated!!!");
		} else {
			ResponseStructure<UserResponce> responseStructure = new ResponseStructure<UserResponce>();
			responseStructure.setStatus(HttpStatus.FOUND.value());
			responseStructure.setMessage("User Id found successfully!!!");
			responseStructure.setData(mapToResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponce>>(responseStructure, HttpStatus.FOUND);
		}
	}

	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> deleteById(int userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserNotFoundByIdException("UserId does not exist!!!"));
		if (user.isDeleated() == true) {
			throw new UserNotFoundByIdException("UserId has already been deleated!!!");
		} else {
			user.setDeleated(true);
			userRepository.save(user);
			ResponseStructure<UserResponce> responseStructure = new ResponseStructure<UserResponce>();
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("User Id deleted successfully!!!");
			responseStructure.setData(mapToResponse(user));
			return new ResponseEntity<ResponseStructure<UserResponce>>(responseStructure, HttpStatus.OK);
		}
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> updateUser(int userId, UserRequest userrequest) {
		User user = userRepository.findById(userId).map(existingUser -> {
			User updatedUser = mapToUser(userrequest);
			updatedUser.setUserId(userId);
			return userRepository.save(updatedUser);
		}).orElseThrow(() -> new UserNotFoundByIdException("User not found by id"));

		ResponseStructure<UserResponce> structure = new ResponseStructure<>();
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("User updated successfully");
		structure.setData(mapToResponse(user));

		return new ResponseEntity<ResponseStructure<UserResponce>>(structure, HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<UserResponce>> addSubjectToTheTeacher(int subjectId, int userId) {
		return subjectRepository.findById(subjectId).map(subject -> {
			User user = userRepository.findById(userId).get();
			if (user.isDeleated() == true)
				throw new UserNotFoundByIdException("UserId has already been deleated!!!");
			if (user.getUserRole().equals(UserRole.TEACHER)) {
				if (user.getSubject() == null) {
					user.setSubject(subject);
					userRepository.save(user);
					subject.getUser().add(user);
					ResponseStructure<UserResponce> responseStructure = new ResponseStructure<UserResponce>();
					responseStructure.setStatus(HttpStatus.OK.value());
					responseStructure.setMessage("User Id deleted successfully!!!");
					responseStructure.setData(mapToResponse(user));
					return new ResponseEntity<ResponseStructure<UserResponce>>(responseStructure, HttpStatus.OK);
				} else {
					throw new IllegalArgumentException("Subject is already assigned to Teacher");
				}
			} else {
				throw new IllegalArgumentException("Subject is added Only to Teacher");
			}
		}).orElseThrow(() -> new IllegalArgumentException("Subject Does Not Exist!!!"));
	}

	private UserResponce mapToResponse(User user) {
		return UserResponce.builder().userId(user.getUserId()).userName(user.getUserName())
				.userEmail(user.getUserEmail()).userContactNo(user.getUserContactNo()).userRole(user.getUserRole())
				.firstName(user.getFirstName()).lastName(user.getLastName())
//				.subject(user.getSubject().getSubjectName())
				.build();
	}

	private User mapToUser(UserRequest userRequest) {
		return User.builder().userName(userRequest.getUserName()).userContactNo(userRequest.getUserContactNo())
				.userEmail(userRequest.getUserEmail()).password(encoded.encode(userRequest.getPassword()))
				.firstName(userRequest.getFirstName()).lastName(userRequest.getLastName())
				.userRole(userRequest.getUserRole()).build();
	}

}
