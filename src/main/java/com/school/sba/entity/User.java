package com.school.sba.entity;

import java.util.List;

import com.school.sba.Enum.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	@Column(unique = true)
	private String userName;
	private String password;
	private String firstName;
	private String lastName;
	private long userContactNo;
	@Column(unique = true)
	private String userEmail;
	@Enumerated(EnumType.STRING)
	private  UserRole userRole;
	private boolean isDeleted;
	@ManyToOne
	private School school;
	@ManyToMany(mappedBy = "user")
	private List<AcademicProgram> academicProgram;
	@ManyToOne
	private Subject subject;
	@OneToMany(mappedBy = "user")
	private List<ClassHour> classHourlist;
}
