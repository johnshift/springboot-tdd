package dev.johnshift.springboottdd.exceptions;

import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** . */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppExceptionHandler extends ResponseEntityExceptionHandler {

  public static final String INVALID_TYPE = "Invalid type provided";

  /*
   * Override Default Exception Handlers here
   * e.g. JsonParse errors, path not found etc. etc.
  */

  @Override
  // @ExceptionHandler(MethodArgumentNotValidException.class)
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, 
      HttpStatus status, WebRequest request
  ) {

    BindingResult result = ex.getBindingResult();
    FieldError fieldError = result.getFieldError();

    ErrorResponse errorResponse = new ErrorResponse();
    errorResponse.setStatus(HttpStatus.BAD_REQUEST);
    errorResponse.setError(INVALID_TYPE); // hide server dev errors

    String field = fieldError.getField();
    Object value = ex.getFieldValue(field);

    errorResponse.setError(ex.getAllErrors().get(0).getDefaultMessage());
    errorResponse.setInfo(field + " = " + value);

    return handleExceptionInternal(ex, errorResponse, headers, status, request);
  }

  /*
   * Handle Global App Exceptions here
   * e.g. Validations etc. etc.
  */

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  protected ResponseEntity<Object> handleMethodArgumentTypeMismatch(
      MethodArgumentTypeMismatchException ex, WebRequest request
  ) {
    return buildResponseEntity(new ErrorResponse(
      HttpStatus.BAD_REQUEST,
      INVALID_TYPE,
      "'" + ex.getName() + "' parameter declared with invalid value of '" + ex.getValue() + "'"
    ));
  }

  /** . */
  @ExceptionHandler(value = ConstraintViolationException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ErrorResponse handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException ex) {
    
    ConstraintViolation<?> violation = ex.getConstraintViolations().iterator().next();

    // get last element of path to be used for info
    final Iterator<?> itr = violation.getPropertyPath().iterator();
    Object key = itr.next();
    while (itr.hasNext()) {
      key = itr.next();
    }

    return new ErrorResponse(
      HttpStatus.BAD_REQUEST,
      violation.getMessage(),
      key + " = " + violation.getInvalidValue()
    );
  }

  private ResponseEntity<Object> buildResponseEntity(ErrorResponse errorResponse) {
    return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
  }
}
