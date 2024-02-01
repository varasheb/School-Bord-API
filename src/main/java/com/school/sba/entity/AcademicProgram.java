package com.school.sba.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.hibernate.annotations.Cascade;
import org.springframework.stereotype.Component;

import com.school.sba.Enum.ProgramType;

import jakarta.persistence.CascadeType;
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
	@Enumerated(EnumType.STRING)
	private ProgramType programType;
	private LocalDate beginsAt;
	private LocalDate endsAt;
	private boolean isDeleted;
	@ManyToOne
	private School school;
	@ManyToMany
	private List<Subject> subjects;
	@ManyToMany
	private List<User> user;
	@OneToMany(mappedBy = "academicProgram",cascade = CascadeType.REMOVE)
	private List<ClassHour> classHour;
}
