package com.school.sba.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SchoolRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.service.AcademicProgramService;
import com.school.sba.service.SchoolService;
import com.school.sba.service.UserService;

import jakarta.transaction.Transactional;

@Component
public class ScheduleJobs {
	@Autowired
	private UserService userService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private AcademicProgramService academicService;
	
//	@Scheduled(fixedDelay = 1000l)
//	public void test() {
//		System.out.println("Test Method !!");
//	}

	@Scheduled(fixedDelay = 1000l*60)
	public void deleteUser() {
	    userService.permantDelete();    
        System.err.println("User DELETED");
	}
	
	@Scheduled(fixedDelay = 1000l*60)
	public void deleteAcademicProgram() {
		academicService.permantDelete();
        System.err.println("AcademicProgram DELETED");
	}
	
	@Scheduled(fixedDelay = 1000l*60)
	public void deleteSchool() {
	    schoolService.permantDelete();	    
        System.err.println("School DELETED");
	}

}
