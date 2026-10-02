package com.andretk.backend1.exceptions;

public class ResourceConflictException extends RuntimeException {
  public ResourceConflictException(String message) {
    super(message);
  }
}
