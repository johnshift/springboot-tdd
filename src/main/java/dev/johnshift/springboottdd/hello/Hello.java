package dev.johnshift.springboottdd.hello;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * Hello is returned as json response.
 */
@Data
@Component
public class Hello {
  private String msg;
}
