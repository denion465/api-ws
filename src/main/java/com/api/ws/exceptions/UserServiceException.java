package com.api.ws.exceptions;

public class UserServiceException extends RuntimeException {
  private final static long serialVersionUID = 1L;

  public UserServiceException(String message) {
    super(message);
  }
}
