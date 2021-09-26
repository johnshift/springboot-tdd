package dev.johnshift.springboottdd.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

/** ... */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserIntegrationTest {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  UserRepository repo;

  @Test
  public void Should_Use_Testx_DB() throws Exception {

    this.webTestClient
      .get()
      .uri("/users")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$[0].username").isEqualTo("testx");
  }

}
