
package org.opensourcecommunity.pomodoroapp.exceptions;

import java.awt.List;
import java.util.Arrays;
import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {
		var errors = new HashMap<String, String>();
		exp.getBindingResult().getAllErrors().forEach(error -> {
			var fieldName = ((FieldError) error).getField();
			var errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);

		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exp) {
		ErrorResponse errorResponse = new ErrorResponse(Arrays.asList(exp.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserSettingsNotFoundException.class)
	public ResponseEntity<?> handleUserSettingsNotFoundException(UserSettingsNotFoundException exp) {
		ErrorResponse errorResponse = new ErrorResponse(Arrays.asList(exp.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(PomodoroSessionNotFoundException.class)
	public ResponseEntity<?> handlePomodoroSessionNotFoundException(PomodoroSessionNotFoundException exp) {
		ErrorResponse errorResponse = new ErrorResponse(Arrays.asList(exp.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(ForbiddenException.class)
	public ResponseEntity<?> handleForbiddenException(ForbiddenException exp) {
		ErrorResponse errorResponse = new ErrorResponse(Arrays.asList(exp.getMessage()));
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
	}

}
