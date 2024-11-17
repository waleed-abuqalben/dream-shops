package com.waleed.dreamshops.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleaccessDeniedException(AccessDeniedException ex) {
		String message = "You Don't have permission to this action!";
		return new ResponseEntity<>(message, HttpStatus.FORBIDDEN);
	}

}
