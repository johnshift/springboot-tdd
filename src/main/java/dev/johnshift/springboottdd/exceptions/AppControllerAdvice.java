package dev.johnshift.springboottdd.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/** . */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class AppControllerAdvice extends ResponseEntityExceptionHandler {

  /*
   * Override Default Exception Handlers here
   * e.g. JsonParse errors, path not found etc. etc.
  */

}
