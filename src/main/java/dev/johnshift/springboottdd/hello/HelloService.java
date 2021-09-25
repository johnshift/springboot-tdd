package dev.johnshift.springboottdd.hello;

import org.springframework.stereotype.Service;

/**
 * HelloService - handles logic for hello package.
 */
@Service
public class HelloService {
  public String createMsg(String name) {
    return "Hello " + name;
  }
}
