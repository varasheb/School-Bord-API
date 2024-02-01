package com.school.sba.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByRoleException;
import com.school.sba.mapper.UserMapper;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestDTO.AcademicProgramRequest;
import com.school.sba.responseDTO.AcademicProgramResponse;
import com.school.sba.responseDTO.UserResponce;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.util.ResponseStructure;

import jakarta.transaction.Transactional;
@Service
public class AcademicProgramServiceImpl implements AcademicProgramService {
	@Autowired
	private AcademicProgramRepository academicProgramRepository;
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserMapper usermapper;
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> addsAcademicProgram(int schoolId,
			AcademicProgramRequest academicProgramRequest) {
		return schoolRepository.findById(schoolId).map(school->{
			AcademicProgram academicProgram = academicProgramRepository.save(mapToAcademicProgram(school, academicProgramRequest));
			school.getAcademicProgram().add(academicProgram);
			schoolRepository.save(school);
			ResponseStructure<AcademicProgramResponse> responseStructure = new ResponseStructure<AcademicProgramResponse>();
			responseStructure.setStatus(HttpStatus.CREATED.value());
			responseStructure.setMessage("AcademicProgram created successfully!!!");
			responseStructure.setData(mapToResponse(academicProgram,false));
			return new ResponseEntity<ResponseStructure<AcademicProgramResponse>>(responseStructure, HttpStatus.CREATED);
		}).orElseThrow(()->new IllegalArgumentException("School Does Not Exist!!!"));
	}
	@Override
	public ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>> fetchAllAcademicProgram(int schoolId) {
		return schoolRepository.findById(schoolId).map(school->{
			List<AcademicProgram> list = school.getAcademicProgram();
			List<AcademicProgramResponse> academicProgramList = new ArrayList<AcademicProgramResponse>();
			for(AcademicProgram a:list) {
				academicProgramList.add(mapToResponse(a,false));
			}
			ResponseStructure<List<AcademicProgramResponse>> responseStructure = new ResponseStructure<List<AcademicProgramResponse>>();
			responseStructure.setStatus(HttpStatus.OK.value());
			responseStructure.setMessage("AcademicProgram fetched successfully!!!");
			responseStructure.setData(academicProgramList);
			return new ResponseEntity<ResponseStructure<List<AcademicProgramResponse>>>(responseStructure, HttpStatus.OK);
		}).orElseThrow(()->new IllegalArgumentException("School Does Not Exist!!!"));
	}
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> assignTeachersStudentsToAcademicProgram(
	        int programId, int userId) {
	    return userRepository.findById(userId).map(user -> {
	        if (user.isDeleted()) {
	            throw new UserNotFoundByIdException("UserId has already been deleted!!!");
	        }
	        if (user.getUserRole().equals(UserRole.ADMIN)) {
	            throw new IllegalArgumentException("Admin cannot be added to AcademicProgram");
	        } else if (user.getUserRole().equals(UserRole.TEACHER)) {
	            // Additional validation for teachers
	            AcademicProgram academicProgram = academicProgramRepository.findById(programId).get();
	            
	            // Check if the teacher teaches one of the subjects offered by the academic program
	            Subject teacherSubject = user.getSubject();
	            if (!academicProgram.getSubjects().contains(teacherSubject)) {
	                throw new IllegalArgumentException("Teacher does not teach a relevant subject for the Academic Program");
	            }
	        }

	        AcademicProgram academicProgram = academicProgramRepository.findById(programId).orElseThrow(() ->
	                new IllegalArgumentException("Academic Program with ID " + programId + " does not exist!!!"));

	        user.getAcademicProgram().add(academicProgram);
	        academicProgram.getUser().add(user);
	        userRepository.save(user);
	        academicProgramRepository.save(academicProgram);

	        List<String> lists = academicProgram.getUser().stream()
	                .map(User::getUserName)
	                .collect(Collectors.toList());

	        ResponseStructure<AcademicProgramResponse> responseStructure = new ResponseStructure<>();
	        responseStructure.setStatus(HttpStatus.OK.value());
	        responseStructure.setMessage("Assigned AcademicProgram successfully!!!");
	        responseStructure.setData(mapToResponseList(academicProgram, lists));
	        return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	    }).orElseThrow(() -> new IllegalArgumentException("User Does Not Exist!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<UserResponce>>> fetchUsersByRoleInAcademicProgram(int programId, String role) {
	    if ("admin".equalsIgnoreCase(role)) {
	        throw new IllegalArgumentException("Cannot fetch users with role 'admin'");
	    }

	    AcademicProgram academicProgram = academicProgramRepository.findById(programId)
	            .orElseThrow(() -> new IllegalArgumentException("Academic Program not found with id: " + programId));

	    List<User> users = userRepository.findByUserRoleAndAcademicProgram(UserRole.valueOf(role.toUpperCase()), academicProgram);

	    if (users.isEmpty()) {
	        throw new UserNotFoundByRoleException("No users found with role '" + role + "' in academic program with id: " + programId);
	    }

	    List<UserResponce> userResponses = users.stream()
	            .map(usermapper::mapToResponse)
	            .collect(Collectors.toList());

	    ResponseStructure<List<UserResponce>> responseStructure = new ResponseStructure<>();
	    responseStructure.setStatus(HttpStatus.OK.value());
	    responseStructure.setMessage("Fetched successfully!!!");
	    responseStructure.setData(userResponses);

	    return new ResponseEntity<>(responseStructure, HttpStatus.OK);
	}
	@Override
	public ResponseEntity<ResponseStructure<AcademicProgramResponse>> deleteById(int academicProgramId) {

		return academicProgramRepository.findById(academicProgramId)
				.map(academicProgram -> {
					academicProgram.setDeleted(true);
					//					repository.deleteById(userId);
					academicProgramRepository.save(academicProgram);
					ResponseStructure<AcademicProgramResponse> structure=new ResponseStructure<>();
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("Academic Program deleted successfully");
					structure.setData(mapToResponse(academicProgram,true));

					return new ResponseEntity<>(structure, HttpStatus.OK);
				}).orElseThrow(()->new IllegalArgumentException("Academic Program not found by id"));
	}
    @Transactional
	@Override
	public void permantDelete() {
		List<AcademicProgram> programs = academicProgramRepository.findByIsDeleted(true);
	    programs.forEach(program -> classHourRepo.deleteAll(program.getClassHour()));
	    academicProgramRepository.deleteAll(programs);
	}
	
	private AcademicProgramResponse mapToResponseList(AcademicProgram academicProgram, List<String> lists) {
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.programName(academicProgram.getProgramName())
				.programType(academicProgram.getProgramType())
				.user(lists)
				.build();
	}
	private AcademicProgramResponse mapToResponse(AcademicProgram academicProgram,boolean isDeleted) {
		return AcademicProgramResponse.builder()
				.programId(academicProgram.getProgramId())
				.beginsAt(academicProgram.getBeginsAt())
				.endsAt(academicProgram.getEndsAt())
				.programName(academicProgram.getProgramName())
				.programType(academicProgram.getProgramType())
				.build();
	}
	private AcademicProgram mapToAcademicProgram(School school, AcademicProgramRequest academicProgramRequest) {
		return AcademicProgram.builder()
				.beginsAt(academicProgramRequest.getBeginsAt())
				.endsAt(academicProgramRequest.getEndsAt())
				.programName(academicProgramRequest.getProgramName())
				.programType(academicProgramRequest.getProgramType())
				.school(school)
				.build();
	}

}
