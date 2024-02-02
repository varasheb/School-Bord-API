package com.school.sba.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.ClassHour;

@Repository
public interface ClassHourRepository extends JpaRepository<ClassHour, Integer> {

	Optional<ClassHour> findTopByOrderByClassHourIdDesc();

	List<ClassHour> findByAcademicProgramAndBeginsAtBetween(AcademicProgram academicProgram, LocalDateTime startDays,
			LocalDateTime endDate);

}
