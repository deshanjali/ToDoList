package com.pearson.ToDoListapi.exceptions;

import java.util.Date;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.pearson.ToDoListapi.responses.ErrorMessage;

@ControllerAdvice
public class AppExceptionsHandler extends ResponseEntityExceptionHandler{
	
	@ExceptionHandler(value= {Exception.class})
	public ResponseEntity<Object> handlingExceptions(Exception ex, WebRequest request){
		String msg = "invalid input - " + ex.getMessage();
		
		ErrorMessage errorMessage = new ErrorMessage(new Date(),msg);
		
		return new ResponseEntity<>(
				errorMessage, new HttpHeaders(),HttpStatus.NOT_FOUND);
	}
}
