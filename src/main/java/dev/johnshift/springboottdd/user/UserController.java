package dev.johnshift.springboottdd.user;

import dev.johnshift.springboottdd.exceptions.ErrorResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
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
  @GetMapping(path = "/users/{id}", produces = "application/json")
  public UserDTO handleGetUser(@PathVariable(name = "id") long id) {

    return svc.getUserById(id);
  }

  /** ... */
  @PostMapping("/users")
  public ResponseEntity<UserDTO> handleCreateUser(@RequestBody UserDTO user) {

    UserDTO createdUser = svc.createUser(user);

    return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
  }

  /** ... */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<UserDTO> handleDeleteUser(@PathVariable(name = "id") long id) {

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /** 
   * Only `bio` field should be updateable. 
   */
  @PutMapping("/users")
  public ResponseEntity<UserDTO> handleUpdateUser(@RequestBody UserDTO user) {

    UserDTO updatedUser = svc.updateUser(user);

    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  // ======================================================================
  // ========================== USER EXCEPTIONS ===========================
  // ======================================================================

  @ExceptionHandler(value = {UserException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handlerAmbot(HttpServletRequest req, Exception ex) {

    return new ErrorResponse(HttpStatus.NOT_FOUND, "some info", ex);
  }
}