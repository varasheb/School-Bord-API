package com.school.sba.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.AcademicProgram;
import com.school.sba.entity.School;
import com.school.sba.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	boolean existsByUserRole(UserRole admin);

	Optional<User> findByUserRole(UserRole admin);

	List<User> findByUserRoleAndAcademicProgram(UserRole valueOf, AcademicProgram program);

	List<User> findByIsDeleted(boolean isDelete);

    List<User> findBySchool(School school);
	
	

}
