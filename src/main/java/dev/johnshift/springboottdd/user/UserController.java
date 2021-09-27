package dev.johnshift.springboottdd.user;

import java.util.List;
import lombok.RequiredArgsConstructor;

import org.apache.catalina.connector.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/** ... */
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService svc;

  /** ... */
  @GetMapping("/users")
  public List<UserDTO> handleGetUsers() {

    return svc.getAllUsers();
  }

  /** ... */
  @GetMapping("/users/{id}")
  public ResponseEntity<UserDTO> handleGetUser(@PathVariable(name = "id") long id) {

    UserDTO user = svc.getUserById(id);

    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  /** ... */
  @PostMapping("/users")
  public ResponseEntity<UserDTO> handleCreateUser(@RequestBody UserDTO user) {

    UserDTO createdUser = svc.createUser(user);

    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }
}