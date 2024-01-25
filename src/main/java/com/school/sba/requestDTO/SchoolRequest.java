package com.school.sba.requestDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolRequest {
	@NotNull(message = "username should not be null")
	private String schoolName;
	private int schoolContactNo;
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email ")
	private String schoolEmailId;
	@NotNull(message = "username should not be null")
	private String schoolAddress;
}
