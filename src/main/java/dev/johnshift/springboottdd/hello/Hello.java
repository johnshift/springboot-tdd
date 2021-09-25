package dev.johnshift.springboottdd.hello;

import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
public class Hello {
  private String msg;
}
