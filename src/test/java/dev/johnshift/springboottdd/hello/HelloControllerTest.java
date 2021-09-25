package dev.johnshift.springboottdd.hello;

// import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class HelloControllerTest {

  @Autowired
  MockMvc mockMvc;

  // @Autowired
  // private ObjectMapper objectMapper;

  @Test
  void itShouldMsgNameParam() throws Exception {

    this.mockMvc.perform(get("/hello")
      .param("name", "John"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.msg", is("Hello John")));
  }

  @Test
  void itShouldMsgDefault() throws Exception {

    this.mockMvc.perform(get("/hello"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.msg", is("Hello World!")));
  }

}
