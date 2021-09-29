package dev.johnshift.springboottdd.user;

/** . */
public class UserException extends RuntimeException {

  public static final String NOT_FOUND = "User not found";

  public static final String ID_REQUIRED = "Id field is required";
  public static final String USERNAME_REQUIRED = "Username field is required";
  public static final String BIO_REQUIRED = "Bio field is required";

  public UserException(String message) {
    super(message);
  }

}

