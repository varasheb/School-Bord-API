package com.school.sba.entity;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Component
@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Data
public class School {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private int schoolContactNo;
	@Column(unique = true)
	private String schoolEmailId;
	private String schoolAddress;
	@OneToOne
	private Scheduleld scheduleld;
	@OneToMany(mappedBy = "school")
	private List<AcademicProgram> academicProgram;
}