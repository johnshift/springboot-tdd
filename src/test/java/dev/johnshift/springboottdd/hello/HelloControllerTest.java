package dev.johnshift.springboottdd.hello;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** ... */
@Import(Hello.class)
@WebMvcTest(HelloController.class)
public class HelloControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  HelloService svc;

  @Test
  void itShouldMsgNameParam() throws Exception {

    when(svc.createMsg(any())).thenReturn("Hello John");

    this.mockMvc.perform(get("/hello").param("name", "John")).andExpect(status().isOk())
      .andExpect(jsonPath("$.msg", is("Hello John")));
  }

  @Test
  void itShouldMsgDefault() throws Exception {
    when(svc.createMsg(any())).thenReturn("Hello World!");

    this.mockMvc.perform(get("/hello")).andExpect(status().isOk())
      .andExpect(jsonPath("$.msg", is("Hello World!")));
  }

}
