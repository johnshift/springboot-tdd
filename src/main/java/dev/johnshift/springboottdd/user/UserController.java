package dev.johnshift.springboottdd.user;

import dev.johnshift.springboottdd.exceptions.ErrorResponse;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
@Validated
public class UserController {

  private final UserService svc;

  /** ... */
  @GetMapping("/users")
  public List<UserDTO> handleGetUsers() {

    return svc.getAllUsers();
  }

  /** ... */
  @GetMapping(path = "/users/{id}", produces = "application/json")
  public UserDTO handleGetUser(@PathVariable(name = "id") @PositiveOrZero long id) {

    return svc.getUserById(id);
  }

  /** ... */
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDTO handleCreateUser(@Valid @RequestBody UserDTO user) {
    return svc.createUser(user);
  }

  /** ... */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<Object> handleDeleteUser(@PathVariable(name = "id") @PositiveOrZero long id) {

    svc.deleteUserById(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /** Only `bio` field should be updateable. */
  @PutMapping("/users")
  @Validated(OnUpdate.class)
  public UserDTO handleUpdateUser(@Valid @RequestBody UserDTO user) {

    return svc.updateUser(user);
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