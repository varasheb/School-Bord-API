package com.school.sba.mapper;

import org.springframework.stereotype.Component;

import com.school.sba.entity.User;
import com.school.sba.responseDTO.UserResponce;
@Component
public class UserMapper {
	public UserResponce mapToResponse(User user) {
		return UserResponce.builder().userId(user.getUserId()).userName(user.getUserName())
				.userEmail(user.getUserEmail()).userContactNo(user.getUserContactNo()).userRole(user.getUserRole())
				.firstName(user.getFirstName()).lastName(user.getLastName())
				.subject(user.getSubject().getSubjectName())
				.build();
	}
}
