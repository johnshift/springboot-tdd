package dev.johnshift.springboottdd.hello;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/** ... */
@ExtendWith(SpringExtension.class)
public class HelloServiceTest {

  @InjectMocks
  HelloService helloService;

  @Test
  public void createMsgTest() throws Exception {

    String name = "John";

    assertEquals("Hello John", helloService.createMsg(name));
  }
}
