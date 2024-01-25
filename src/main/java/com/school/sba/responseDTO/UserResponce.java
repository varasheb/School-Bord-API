package com.school.sba.responseDTO;

import com.school.sba.Enum.UserRole;
import com.school.sba.entity.Subject;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponce {
	private int userId;
	private String userName;
	private String firstName;
	private String lastName;
	private long userContactNo;
	private String userEmail;
	private UserRole userRole;
	private String subject;
}
