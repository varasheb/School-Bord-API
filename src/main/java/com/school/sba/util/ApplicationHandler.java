package com.school.sba.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.school.sba.exception.AdminFoundException;
import com.school.sba.exception.IllegalArgumentException;
import com.school.sba.exception.SchoolNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;

@RestControllerAdvice
public class ApplicationHandler extends ResponseEntityExceptionHandler {
	private ResponseEntity<Object> structure (HttpStatus status,String message,Object rootCause){
		return new ResponseEntity<Object> (Map.of(
				"status",status.value(),
				"message",message,
				"rootCause",rootCause),status);		
	}
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			org.springframework.http.HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<ObjectError> allErrors = ex.getAllErrors();

		Map<String, String> errors= new  HashMap<String,String>();		
		allErrors.forEach(error ->{
			FieldError fieldError=(FieldError) error;
			errors.put(fieldError.getField(), fieldError.getDefaultMessage());
		});
		return structure(HttpStatus.BAD_REQUEST,"failed to save the data",errors);
	}
	@ExceptionHandler (AdminFoundException.class)
	public ResponseEntity<Object> handlerAdminNotFoundById(AdminFoundException ex){
		return structure(HttpStatus.FOUND,ex.getMessage(),"Only one ADMIN can be allowed");
	}
	@ExceptionHandler (UserNotFoundByIdException.class)
	public ResponseEntity<Object> handlerUserNotFoundById(UserNotFoundByIdException ex){
		return structure(HttpStatus.NOT_FOUND,ex.getMessage(),"User Doesn't Exist!!!");
	}
	@ExceptionHandler (IllegalArgumentException.class)
	public ResponseEntity<Object> handlerUserNotFoundById(IllegalArgumentException ex){
		return structure(HttpStatus.BAD_REQUEST,ex.getMessage(),"Request Not Applicable");
	}
}