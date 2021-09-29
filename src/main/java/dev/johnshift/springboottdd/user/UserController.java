package dev.johnshift.springboottdd.user;

import dev.johnshift.springboottdd.exceptions.ErrorResponse;
import java.util.List;
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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

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
  public UserDTO handleGetUser(
      @PathVariable(name = "id") @PositiveOrZero Long id,
      WebRequest request
  ) {

    request.setAttribute("id", id, RequestAttributes.SCOPE_REQUEST);
    return svc.getUserById(id);
  }

  /** ... */
  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDTO handleCreateUser(@Valid @RequestBody UserDTO user, WebRequest request) {
    return svc.createUser(user);
  }

  /** ... */
  @DeleteMapping("/users/{id}")
  public ResponseEntity<Object> handleDeleteUser(
      @PathVariable(name = "id") @PositiveOrZero Long id,
      WebRequest req
  ) {

    req.setAttribute("id", id, RequestAttributes.SCOPE_REQUEST);
    svc.deleteUserById(id);

    return new ResponseEntity<>(HttpStatus.OK);
  }

  /** Only `bio` field should be updateable. */
  @PutMapping("/users")
  @Validated(OnUpdate.class)
  public UserDTO handleUpdateUser(@Valid @RequestBody UserDTO user, WebRequest req) {

    req.setAttribute("username", user.getUsername(), RequestAttributes.SCOPE_REQUEST);

    return svc.updateUser(user);
  }

  // ======================================================================
  // ========================== USER EXCEPTIONS ===========================
  // ======================================================================

  /** . */
  @ExceptionHandler(value = {UserException.class})
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ErrorResponse handlerAmbot(WebRequest req, Exception ex) {

    int scope = RequestAttributes.SCOPE_REQUEST;

    Object username = req.getAttribute("username", scope);
    if (username != null) {
      return new ErrorResponse(
        HttpStatus.NOT_FOUND, 
        "Username '" + username + "' does not exists", 
        ex
      );
    } 
    
    Object id = req.getAttribute("id", scope);
    if (id != null) {
      return new ErrorResponse(
        HttpStatus.NOT_FOUND, 
        "No user found with id = " + id,
        ex
      );
    }

    return new ErrorResponse(HttpStatus.NOT_FOUND, "User does not exist in database", ex);
  }

}