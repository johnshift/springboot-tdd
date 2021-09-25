package dev.johnshift.springboottdd.hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class HelloController {

  @Autowired
  Hello hello;

  @Autowired
  HelloService svc;

  @GetMapping("/hello")
  public Hello helloHandler(@RequestParam(value = "name", defaultValue = "World!") String name) {
    hello.setMsg(svc.createMsg(name));

    return hello;
  }
}
