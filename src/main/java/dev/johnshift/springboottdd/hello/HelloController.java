package dev.johnshift.springboottdd.hello;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** ... */
@RestController
@RequiredArgsConstructor
public class HelloController {

  @Autowired
  Hello hello;

  @Autowired
  HelloService svc;

  /**
   * Returns a json response using Hello class. 
   * "/hello?name=John" -> {"msg": "Hello John"} 
   * "/hello" -> {"msg": "Hello World!"} 
   */
  @GetMapping("/hello")
  public Hello helloHandler(@RequestParam(value = "name", defaultValue = "World!") String name) {
    hello.setMsg(svc.createMsg(name));

    return hello;
  }
}
