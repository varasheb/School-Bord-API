package com.school.sba.requestDTO;

import com.school.sba.Enum.UserRole;
//import com.school.sba.entity.School;
import com.school.sba.entity.School;
import com.school.sba.entity.Subject;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRequest {
	@NotNull(message = "username should not be null")
	@Pattern(regexp = "^[a-zA-Z0-9]+$",message = "UserName Should be alphaNumeric value")
	private String userName;
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", message = "Password must"
			+ " contain at least one small letter, one capital letter, one number, one special character")
	private String password;
	@Pattern(regexp = "^[A-Z][a-zA-Z ]*$",message = "must be upper camel case and numbers are not allowed")
	private String firstName;
	@Pattern(regexp = "^[A-Z][a-zA-Z ]*$",message = "must be upper camel case and numbers are not allowed")
	private String lastName;
	private long userContactNo;
	@Email(regexp = "[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.[a-z]{2,}", message = "invalid email ")
	private String userEmail;
	private  UserRole userRole;
	private School school;
	private Subject subject;
}
