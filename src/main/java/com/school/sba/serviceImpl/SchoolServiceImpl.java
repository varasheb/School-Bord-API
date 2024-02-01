package com.school.sba.serviceImpl;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.School;
import com.school.sba.entity.User;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestDTO.SchoolRequest;
import com.school.sba.responseDTO.SchoolResponce;
import com.school.sba.service.SchoolService;
import com.school.sba.util.ResponseStructure;

import jakarta.transaction.Transactional;

@Service
public class SchoolServiceImpl implements SchoolService{
	@Autowired
	private SchoolRepository schoolRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private AcademicProgramRepository academicProgramRepo;

	private School mapToSchool(SchoolRequest schoolRequest) {
		return School.builder()
				.schoolName(schoolRequest.getSchoolName())
				.schoolEmailId(schoolRequest.getSchoolEmailId())
				.schoolAddress(schoolRequest.getSchoolAddress())
				.schoolContactNo(schoolRequest.getSchoolContactNo())
				.build();
	}
	private SchoolResponce mapToResponse(School school,boolean isDeleted) {
		return SchoolResponce.builder()
				.schoolId(school.getSchoolId())
				.schoolName(school.getSchoolName())
				.schoolAddress(school.getSchoolAddress())
				.schoolContactNo(school.getSchoolContactNo())
				.schoolEmailId(school.getSchoolEmailId())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponce>> adminCreatesSchool(int userId,
			SchoolRequest schoolRequest) {
		return userRepository.findById(userId).map(user->{
			if(user.isDeleted()==true)
				throw new UserNotFoundByIdException("UserId has already been deleated!!!");
			if(user.getUserRole().equals(UserRole.ADMIN)) {
				if(user.getSchool()==null) {
					School school = schoolRepository.save(mapToSchool(schoolRequest));
					user.setSchool(school);
					userRepository.save(user);
					ResponseStructure<SchoolResponce> responseStructure = new ResponseStructure<SchoolResponce>();
					responseStructure.setStatus(HttpStatus.CREATED.value());
					responseStructure.setMessage("Student Object Created Successfully");
					responseStructure.setData(mapToResponse(school,false));
					return new ResponseEntity<ResponseStructure<SchoolResponce>>(responseStructure,HttpStatus.CREATED);
				}else
					throw new IllegalArgumentException("Admin has already accessed to school");
			}else
				throw new IllegalArgumentException("Only Admin can access to school");
		}).orElseThrow(()-> new UserNotFoundByIdException("UserId does not exist!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<SchoolResponce>> deleteById(int schoolId) {
		return schoolRepository.findById(schoolId)
				.map(school -> {
					school.setDeleted(true);
					schoolRepository.save(school);
					ResponseStructure<SchoolResponce> structure = new ResponseStructure<SchoolResponce>();
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("School deleted successfully");
					structure.setData(mapToResponse(school,true));

					return new ResponseEntity<>(structure, HttpStatus.OK);
				}).orElseThrow(()->new IllegalArgumentException("School not found by id"));
	}
	@Transactional
	@Override
	public void permantDelete() {
		schoolRepository.findByIsDeleted(true).forEach(school->{
			academicProgramRepo.deleteAll(school.getAcademicProgram());
			List<User> users = userRepository.findBySchool(school);
			users.forEach(user->{
				if(user.getUserRole().equals(UserRole.ADMIN)) {
				user.setSchool(null);
				userRepository.save(user);
				}else {
					userRepository.delete(user);
				}
			});
			schoolRepository.delete(school);
		});

	}
}