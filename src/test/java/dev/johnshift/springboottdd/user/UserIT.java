package dev.johnshift.springboottdd.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.johnshift.springboottdd.exceptions.AppExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/** ... */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
@Sql({"/db/users_schema.sql", "/db/users_sample.sql"})
public class UserIT {

  @LocalServerPort
  int port;

  @Autowired
  WebTestClient webTestClient;

  WebClient webClient = WebClient.create();

  @Autowired
  UserRepository repo;

  @Autowired
  ObjectMapper jsonMapper = new ObjectMapper();

  @Container
  private static MySQLContainer<?> container = new MySQLContainer<>("mysql:latest");

  /**  overide application.yml on test run */
  @DynamicPropertySource
  public static void overrideProps(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", container::getJdbcUrl);
    registry.add("spring.datasource.username", container::getUsername);
    registry.add("spring.datasource.password", container::getPassword);
  }

  @Test
  public void get_users_OK() throws Exception {

    webTestClient
      .get()
      .uri("/users")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$[0].id").isEqualTo(1L)
      .jsonPath("$[1].id").isEqualTo(2L)
      .jsonPath("$[2].id").isEqualTo(3L)
      .jsonPath("$[0].username").isEqualTo("user1")
      .jsonPath("$[1].username").isEqualTo("user2")
      .jsonPath("$[2].username").isEqualTo("user3")
      .jsonPath("$[0].bio").isEqualTo("looking for a job")
      .jsonPath("$[1].bio").isEqualTo("coding 16hrs a day")
      .jsonPath("$[2].bio").isEqualTo("gorgeous creature");
  }

  @Test
  public void get_user_OK() throws Exception {

    webTestClient
      .get()
      .uri("/users/1")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.id").isEqualTo(1L)
      .jsonPath("$.username").isEqualTo("user1")
      .jsonPath("$.bio").isEqualTo("looking for a job");
  }

  @Test
  public void get_nonExistingUser_throw_NotFound() throws Exception {

    final Long id = 69L;
    final String info = "No user found with id = " + id;

    webTestClient
      .get()
      .uri("/users/" + id)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.name())
      .jsonPath("$.error").isEqualTo(UserException.NOT_FOUND)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();
  }

  @Test
  public void get_user_byInvalidId_throw_BadRequest() throws Exception {

    final String info = "'id' parameter declared with invalid value of 'asdf'";

    webTestClient
      .get()
      .uri("/users/asdf")
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(AppExceptionHandler.INVALID_TYPE)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();
  }

  @Test
  public void post_user_OK() throws Exception {
    
    UserDto user = new UserDto();
    user.setUsername("johnshift");
    user.setBio("looking for a job");

    webTestClient
      .post()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isCreated()
      .expectBody()
      .jsonPath("$.id").isNumber()
      .jsonPath("$.username").isEqualTo(user.getUsername())
      .jsonPath("$.bio").isEqualTo(user.getBio());


    // confirm via get
    Long id = 4L; // only 3 rows were pre-added, id = 4 will be next
    UserDto createdUser = webClient
        .get()
        .uri("http://localhost:" + port + "/users/" + id)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(UserDto.class)
        .block();

    assertEquals(id, createdUser.getId());
    assertEquals(user.getUsername(), createdUser.getUsername());
    assertEquals(user.getBio(), createdUser.getBio());
  }
  
  @Test
  public void post_user_noUsername_throw_RequiredUsername() throws Exception {
    
    UserDto user = new UserDto();
    user.setBio("looking for a job");

    webTestClient
      .post()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(UserException.USERNAME_REQUIRED)
      .jsonPath("$.info").isEqualTo("username = null")
      .jsonPath("$.timestamp").exists();

  }

  // todo: post /user bio 
  @Test
  public void post_user_noBio_throw_RequiredBio() throws Exception {
    
    UserDto user = new UserDto();
    user.setUsername("johnshift");

    webTestClient
      .post()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(UserException.BIO_REQUIRED)
      .jsonPath("$.info").isEqualTo("bio = null")
      .jsonPath("$.timestamp").exists();

  }

  // todo: delete /user invalid id
  @Test
  public void delete_user_byInvalidId_throw_BadRequest() throws Exception {

    String info = "'id' parameter declared with invalid value of 'asdf'";

    webTestClient
      .delete()
      .uri("/users/asdf")
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(AppExceptionHandler.INVALID_TYPE)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();
  }

  // todo: delete /user ok
  @Test
  public void delete_user_OK() {

    // given
    final Long id = 1L;
    final String info = "No user found with id = " + id;

    // delete user
    webTestClient
      .delete()
      .uri("/users/" + id)
      .exchange()
      .expectStatus().isOk();

    // confirm deleted via get
    webTestClient
      .get()
      .uri("/users/" + id)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.name())
      .jsonPath("$.error").isEqualTo(UserException.NOT_FOUND)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();

  }

  // todo: delete /user nonexisting
  @Test
  public void delete_nonExistingUser_throw_NotFound() throws Exception {

    final Long nonExistingId = 69L;
    final String info = "No user found with id = " + nonExistingId;

    webTestClient
      .delete()
      .uri("/users/" + nonExistingId)
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.name())
      .jsonPath("$.error").isEqualTo(UserException.NOT_FOUND)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();
  }

  // todo: put /user ok
  @Test
  public void put_user_should_OnlyChangeBio() throws Exception {
    
    // retrieve user
    UserDto user = webClient
        .get()
        .uri("http://localhost:" + port + "/users/1")
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(UserDto.class)
        .block();

    // update user
    String newBio = "More handsome than ever";
    user.setBio(newBio);
    webTestClient
      .put()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.id").isEqualTo(user.getId())
      .jsonPath("$.username").isEqualTo(user.getUsername())
      .jsonPath("$.bio").isEqualTo(newBio);


    // confirm by getting user
    webTestClient
      .get()
      .uri("/users/1")
      .exchange()
      .expectStatus().isOk()
      .expectBody()
      .jsonPath("$.id").isEqualTo(user.getId())
      .jsonPath("$.username").isEqualTo(user.getUsername())
      .jsonPath("$.bio").isEqualTo(newBio);

  }

  
  // todo: put /user nonexisting
  @Test
  public void put_nonExistingUser_throw_NotFound() throws Exception {

    // given
    Long nonExistingId = 69L;
    String username = "johnshift";
    final String info = "Username '" + username + "' does not exists";

    UserDto user = new UserDto(nonExistingId, "johnshift", "looking for a job");

    webTestClient
      .put()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isNotFound()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.NOT_FOUND.name())
      .jsonPath("$.error").isEqualTo(UserException.NOT_FOUND)
      .jsonPath("$.info").isEqualTo(info)
      .jsonPath("$.timestamp").exists();

  }

  // todo: put /user id required
  @Test
  public void put_user_noId_throw_RequiredID() throws Exception {

    UserDto user = new UserDto();
    user.setUsername("johnshift");
    user.setBio("looking for a job");

    webTestClient
      .put()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(UserException.ID_REQUIRED)
      .jsonPath("$.info").isEqualTo("id = null")
      .jsonPath("$.timestamp").exists();

  }

  // todo: put /user username required
  @Test
  public void put_user_noUsername_throw_RequiredUsername() throws Exception {

    UserDto user = new UserDto();
    user.setId(1L);
    user.setBio("looking for a job");

    webTestClient
      .put()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(UserException.USERNAME_REQUIRED)
      .jsonPath("$.info").isEqualTo("username = null")
      .jsonPath("$.timestamp").exists();

  }

  // todo: put /user bio required
  @Test
  public void put_user_noBio_throw_RequiredBio() throws Exception {

    UserDto user = new UserDto();
    user.setId(1L);
    user.setUsername("johnshift");

    webTestClient
      .put()
      .uri("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(user))
      .exchange()
      .expectStatus().isBadRequest()
      .expectBody()
      .jsonPath("$.status").isEqualTo(HttpStatus.BAD_REQUEST.name())
      .jsonPath("$.error").isEqualTo(UserException.BIO_REQUIRED)
      .jsonPath("$.info").isEqualTo("bio = null")
      .jsonPath("$.timestamp").exists();

  }

}
