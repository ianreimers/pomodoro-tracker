package org.opensourcecommunity.pomodoroapp.exceptions;

public class EmailExistsException extends RuntimeException {

  public EmailExistsException(String message) {
    super(message);
  }
}
