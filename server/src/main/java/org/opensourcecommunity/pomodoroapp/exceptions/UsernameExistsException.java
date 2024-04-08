package org.opensourcecommunity.pomodoroapp.exceptions;

public class UsernameExistsException extends RuntimeException {

  public UsernameExistsException(String message) {
    super(message);
  }
}
