package dev.johnshift.springboottdd.hello;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
  public String createMsg(String name) {
    return "Hello " + name;
  }
}
