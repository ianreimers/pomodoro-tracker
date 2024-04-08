package org.opensourcecommunity.pomodoroapp.exceptions;

import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

  private ResponseEntity<ErrorResponse> buildResponse(
      String message, HttpStatus status, WebRequest request) {
    ErrorResponse errorResponse = new ErrorResponse(message, request.getDescription(false));
    return new ResponseEntity<>(errorResponse, status);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exp, WebRequest request) {
    String errors =
        exp.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining("; "));

    if (errors.isEmpty()) {
      errors = "Validation error";
    }

    return buildResponse(errors, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
      InvalidCredentialsException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFoundException(
      UserNotFoundException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(UserSettingsNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserSettingsNotFoundException(
      UserSettingsNotFoundException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(PomodoroSessionNotFoundException.class)
  public ResponseEntity<ErrorResponse> handlePomodoroSessionNotFoundException(
      PomodoroSessionNotFoundException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.NOT_FOUND, request);
  }

  @ExceptionHandler(EmailExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailExistsException(
      EmailExistsException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(UsernameExistsException.class)
  public ResponseEntity<ErrorResponse> handleEmailExistsException(
      UsernameExistsException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<?> handleForbiddenException(ForbiddenException exp, WebRequest request) {
    return buildResponse(exp.getMessage(), HttpStatus.FORBIDDEN, request);
  }
}
