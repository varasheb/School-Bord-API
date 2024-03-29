package com.school.sba.serviceImpl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.Enum.ClassStatus;
import com.school.sba.Enum.UserRole;
import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;
import com.school.sba.entity.Scheduleld;
import com.school.sba.entity.Subject;
import com.school.sba.entity.User;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.mapper.UserMapper;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.requestDTO.ClassHourDTO;
import com.school.sba.requestDTO.ExelRequestDTO;
import com.school.sba.responseDTO.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.util.ResponseStructure;

@Service
public class ClassHourServiceImpl implements ClassHourService {

	@Autowired
	private ClassHourRepository classHourRepo;
	@Autowired
	private SubjectRepository subjectRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private UserMapper usermapper;

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

		// Find the next Monday from the starting date
		LocalDate nextMonday = startingDate.with(TemporalAdjusters.next(DayOfWeek.MONDAY));

		for (int dayOfWeek = 0; dayOfWeek < 6; dayOfWeek++) { // Iterate from Monday to Saturday

			LocalDate currentDate = nextMonday.plusDays(dayOfWeek);
			LocalTime opensAt = schedule.getOpensAt();
			LocalTime endsAt = opensAt.plus(classDuration);
			ClassStatus status = ClassStatus.NOT_SCHEDULED;

			for (int classPerDay = schedule.getClassHoursPerDay(); classPerDay > 0; classPerDay--) {
				ClassHour classHour = ClassHour.builder()
						.beginsAt(LocalDateTime.of(currentDate, opensAt))
						.endsAt(LocalDateTime.of(currentDate, endsAt))
						.roomNo(null)
						.classStatus(status)
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

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourDTO> classHourDtoList) {
		List<ClassHourResponse> updatedClassHourResponses = new ArrayList<>();

		classHourDtoList.forEach(classHourDTO -> {
			ClassHour existingClassHour =classHourRepo.findById(classHourDTO.getClassHourID()).get();
			Subject subject=subjectRepo.findById(classHourDTO.getSubjectId()).get();
			User teacher=userRepo.findById(classHourDTO.getTeacherId()).get();
			if(existingClassHour.getClassStatus()==ClassStatus.NOT_SCHEDULED)
				if(existingClassHour!=null&&subject!=null&&teacher!=null&&teacher.getUserRole().equals(UserRole.TEACHER)) {
					if((teacher.getSubject()).equals(subject))
						existingClassHour.setSubject(subject);
					else
						throw new IllegalArgumentException("The Teacher is Not Teaching That Subject");
					existingClassHour.setUser(teacher);
					existingClassHour.setRoomNo(classHourDTO.getRoomNo());
					LocalDateTime currentTime = LocalDateTime.now();
					if (existingClassHour.getBeginsAt().isBefore(currentTime) && existingClassHour.getEndsAt().isAfter(currentTime)) {
						existingClassHour.setClassStatus(ClassStatus.ONGOING);
					} else if (existingClassHour.getEndsAt().isBefore(currentTime)) {
						existingClassHour.setClassStatus(ClassStatus.COMPLETED);
					} else {
						existingClassHour.setClassStatus(ClassStatus.SCHEDULED);
					}
					existingClassHour=classHourRepo.save(existingClassHour);
					ClassHourResponse response=mapToResponse(existingClassHour);
					response.setSubject(existingClassHour.getSubject().getSubjectName());
					response.setTeacher(usermapper.mapToResponse(teacher));
					updatedClassHourResponses.add(response);
				} else {
					throw new IllegalArgumentException("Invalid ClassHourID or Invalid User or Invalid Subject");
				} else {
					throw new IllegalArgumentException("Class is already Sheduled");
				}
		});
		ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours updated successfully!!!!");
		responseStructure.setData(updatedClassHourResponses);

		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}

	private ClassHourResponse mapToResponse(ClassHour classHour) {
		return ClassHourResponse.builder().classHourId(classHour.getClassHourId()).beginsAt(classHour.getBeginsAt())
				.endsAt(classHour.getEndsAt()).roomNo(classHour.getRoomNo()).classStatus(classHour.getClassStatus())
				.build();
	}

	@Override
	public ResponseEntity<ResponseStructure<String>> deleteAll() {
		classHourRepo.deleteAll();
		ResponseStructure<String> responseStructure = new ResponseStructure<String>();
		responseStructure.setStatus(HttpStatus.OK.value());
		responseStructure.setMessage("classHour deleted successfully!!!");
		responseStructure.setData("All ClassHour Deleted");
		return new ResponseEntity<ResponseStructure<String>>(responseStructure, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> repeatClassHour() {
		ClassHour lastRecord = classHourRepo.findTopByOrderByClassHourIdDesc().get();
		LocalDate endDate =lastRecord.getBeginsAt().toLocalDate();
		AcademicProgram academicProgram=lastRecord.getAcademicProgram();
		List<ClassHour> previousWeekClassHours = classHourRepo.findByAcademicProgramAndBeginsAtBetween(academicProgram, endDate.minusDays(5).atStartOfDay(), endDate.atStartOfDay().plusDays(1));
		if (previousWeekClassHours.isEmpty()) {
			throw new IllegalArgumentException("The classHour Is Empty");
		}
		List<ClassHour> savedClassHours = new ArrayList<>();
		List<ClassHour> nextWeekClassHours = generateClassHoursForWeek(previousWeekClassHours.get(0).getAcademicProgram(), endDate);
		nextWeekClassHours=classHourRepo.saveAll(nextWeekClassHours);

		for (ClassHour nextWeek : nextWeekClassHours) {
			for (ClassHour previous : previousWeekClassHours) {
				if(nextWeek.getBeginsAt().getDayOfWeek().equals(previous.getBeginsAt().getDayOfWeek())&&nextWeek.getBeginsAt().toLocalTime().equals(previous.getBeginsAt().toLocalTime())) {
					if(previous.getClassStatus()!=ClassStatus.NOT_SCHEDULED) {
						nextWeek.setRoomNo(previous.getRoomNo());
						nextWeek.setClassStatus(ClassStatus.SCHEDULED);
						nextWeek.setUser(previous.getUser());
						nextWeek.setSubject(previous.getSubject());
						ClassHour classHour=classHourRepo.save(nextWeek);
						savedClassHours.add(classHour);
					}
				}

			}
		}


		List<ClassHourResponse> classHourResponses = savedClassHours.stream().map(this::mapToResponse).collect(Collectors.toList());

		ResponseStructure<List<ClassHourResponse>> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("ClassHours repeated successfully!!!!");
		responseStructure.setData(classHourResponses);

		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}


	@Override
	public ResponseEntity<ResponseStructure<String>> updateClassHourInExel(int programId,ExelRequestDTO exelRequestDTO) {

		LocalDateTime startDateTime=exelRequestDTO.getFromDate().atStartOfDay();
		LocalDateTime endDateTime=exelRequestDTO.getToDate().atStartOfDay().plusDays(1);
		AcademicProgram program=academicProgramRepository.findById(programId).get();
		List<ClassHour> listClassHours = classHourRepo.findByAcademicProgramAndBeginsAtBetween(program, startDateTime, endDateTime);
		XSSFWorkbook workbook=new XSSFWorkbook();
		Sheet sheet=workbook.createSheet();

		int rowNumber=0;
		Row header=sheet.createRow(rowNumber);
		header.createCell(0).setCellValue("Begin Date");
		header.createCell(1).setCellValue("Begin Time");
		header.createCell(2).setCellValue("End Date");
		header.createCell(3).setCellValue("End Time");
		header.createCell(4).setCellValue("Subject");
		header.createCell(5).setCellValue("Teacher");
		header.createCell(6).setCellValue("Room No");

		DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm"); 
		DateTimeFormatter date = DateTimeFormatter.ofPattern("dd-MM-yyyy");

		for(ClassHour classHour : listClassHours) {
			Row row = sheet.createRow(++rowNumber);
			row.createCell(0).setCellValue(date.format(classHour.getBeginsAt()));
			row.createCell(1).setCellValue(time.format(classHour.getBeginsAt()));
			row.createCell(2).setCellValue(date.format(classHour.getEndsAt()));
			row.createCell(3).setCellValue(time.format(classHour.getEndsAt()));
			if(classHour.getSubject()==null&&classHour.getUser()==null) {
				row.createCell(4).setCellValue("");
				row.createCell(5).setCellValue("");	
			}else {
				row.createCell(4).setCellValue(classHour.getSubject().getSubjectName());
				row.createCell(5).setCellValue(classHour.getUser().getUserName());	
			}
			row.createCell(6).setCellValue(classHour.getRoomNo());
		}
		try {
			FileOutputStream fileOut;
			fileOut = new FileOutputStream(exelRequestDTO.getFilePath());
			workbook.write(fileOut);
			fileOut.close();
		} catch (IOException e) {
			throw new IllegalArgumentException("Failed To Store in Exel Sheet");
		}
		ResponseStructure<String> responseStructure = new ResponseStructure<>();
		responseStructure.setStatus(HttpStatus.CREATED.value());
		responseStructure.setMessage("Sucefully Saved in Exel Sheet !!!!");
		responseStructure.setData("Sucefully Saved in Exel Sheet from Date :"+exelRequestDTO.getFromDate()+" To Date:"+exelRequestDTO.getToDate());

		return new ResponseEntity<>(responseStructure, HttpStatus.CREATED);
	}






}

