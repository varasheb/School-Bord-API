package com.school.sba.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.Subject;
@Repository
public interface AcademicProgramRepository extends JpaRepository<AcademicProgram, Integer> {

	List<AcademicProgram> findByIsDeleted(boolean b);

}
