package dev.johnshift.springboottdd.exceptions;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/** . */
@Getter
@Setter
public class ErrorResponse {

  private HttpStatus status;
  private String error;
  private String info;
  private Date timestamp;

  public ErrorResponse() {
    timestamp = new Date();
  }

  /** . */
  public ErrorResponse(HttpStatus status, String message, String info) {
    this();
    this.status = status;
    this.error = message;
    this.info = info;
  }

  /** . */
  public ErrorResponse(HttpStatus status, String info, Throwable ex) {
    this();
    this.status = status;
    this.error = ex.getMessage();
    this.info = info;
  }
}
