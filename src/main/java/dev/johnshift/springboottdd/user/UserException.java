package dev.johnshift.springboottdd.user;

/** . */
public class UserException extends RuntimeException {

  public static final String NOT_FOUND = "User not found";

  public UserException() {
    super();
  }

  public UserException(String message) {
    super(message);
  }

}

