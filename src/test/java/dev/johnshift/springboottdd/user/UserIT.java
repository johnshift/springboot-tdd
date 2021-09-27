package dev.johnshift.springboottdd.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** ... */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql({"/db/users_schema.sql", "/db/users_sample.sql"})
public class UserIT {

  @Autowired
  WebTestClient webTestClient;

  @Autowired
  UserRepository repo;

  @Container
  private static MySQLContainer<?> container = new MySQLContainer<>("mysql:latest");

  /**  overide application.yml on test run */
  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }


  // @Test
  // public void someTest() {
  //   int two = 2;
  //   assertEquals(two, 2);
  // }
  
  @Test
  public void shouldReturnAllUsers() throws Exception {
    
    this.webTestClient
      .get()
      .uri("/users")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$[0].username").isEqualTo("user1")
      .jsonPath("$[1].username").isEqualTo("user2")
      .jsonPath("$[2].username").isEqualTo("user3")
      .jsonPath("$[0].bio").isEqualTo("looking for a job")
      .jsonPath("$[1].bio").isEqualTo("coding 16hrs a day")
      .jsonPath("$[2].bio").isEqualTo("gorgeous creature");
  }

}
