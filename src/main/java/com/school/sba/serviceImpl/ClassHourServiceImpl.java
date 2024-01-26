package com.school.sba.serviceImpl;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.Enum.ClassStatus;
import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Scheduleld;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.responseDTO.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private ClassHourRepository classHourRepo;

	@Autowired
	private AcademicProgramRepository academicProgramRepository;

	private List<ClassHour> generateClassHoursForWeek(AcademicProgram program, LocalDate startingDate) {
		List<ClassHour> classHours = new ArrayList<>();
		Scheduleld schedule = program.getSchool().getScheduleld();
		Duration classDuration = schedule.getClassHourLengthInMin();
		Duration lunchDuration = schedule.getLunchLengthInMin();
		Duration breakDuration = schedule.getBreakLengthInMin();
		LocalTime breakTime = schedule.getBreakTime();
		LocalTime lunchTime = schedule.getLunchTime();
		Duration topUp = Duration.ofMinutes(2);

		for (int dayOfWeek = 0; dayOfWeek < 7; dayOfWeek++) {
			
			LocalDate currentDate = startingDate.plusDays(dayOfWeek);
			LocalTime opensAt = schedule.getOpensAt();
			LocalTime endsAt = opensAt.plus(classDuration);
			for (int classperday = schedule.getClassHoursPerDay(); classperday > 0; classperday--) {
				ClassHour classHour = ClassHour.builder()
						.beginsAt(LocalDateTime.of(currentDate, opensAt))
						.endsAt(LocalDateTime.of(currentDate, endsAt))
						.roomNo(null)
						.classStatus(ClassStatus.NOT_SCHEDULED)
						.academicProgram(program)
						.build();
				
				classHours.add(classHour);
				
				if (breakTime.isAfter(opensAt.minus(topUp)) && breakTime.isBefore(endsAt.plus(topUp))) {
					opensAt = opensAt.plus(breakDuration);
					endsAt = endsAt.plus(breakDuration);
				} else if (lunchTime.isAfter(opensAt.minus(topUp)) && lunchTime.isBefore(endsAt.plus(topUp))) {
					opensAt = opensAt.plus(lunchDuration);
					endsAt = endsAt.plus(lunchDuration);
				}
			
				opensAt = endsAt;
				endsAt = opensAt.plus(classDuration);
			}
		}

		return classHours;
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> createClassHoursForWeek(int programId) {
		AcademicProgram program = academicProgramRepository.findById(programId).orElse(null);

		if (program == null)
			throw new IllegalArgumentException("Program Does Not Exist!!!");
		
		LocalDate recordStartDate = (program.getBeginsAt().isAfter(LocalDate.now()))? program.getBeginsAt() : LocalDate.now();
		
		List<ClassHour> generatedClassHours = generateClassHoursForWeek(program, recordStartDate);
		List<ClassHour> savedClassHours = classHourRepo.saveAll(generatedClassHours);

		List<ClassHourResponse> classHourResponses = savedClassHours.stream().map(this::mapToResponse)
				.collect(Collectors.toList());

		ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours created successfully!!!!");
		responseStructure.setData(classHourResponses);

		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	private ClassHourResponse mapToResponse(ClassHour classHour) {
		return ClassHourResponse.builder().classHourId(classHour.getClassHourId()).beginsAt(classHour.getBeginsAt())
				.endsAt(classHour.getEndsAt()).roomNo(classHour.getRoomNo()).classStatus(classHour.getClassStatus())
				.build();
	}
}
