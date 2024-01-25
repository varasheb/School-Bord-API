package com.school.sba.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByUserName(String username);

	boolean existsByUserRole(UserRole admin);

	Optional<User> findByUserRole(UserRole admin);

}
