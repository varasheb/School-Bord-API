package com.school.sba.entity;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.school.sba.Enum.ProgramType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AcademicProgram {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	private String programName;
	private ProgramType programType;
	private LocalTime beginsAt;
	private LocalTime endsAt;
	@ManyToOne
	private School school;
	@ManyToMany
	private List<Subject> subjects;
	@ManyToMany
	private List<User> user;
}
