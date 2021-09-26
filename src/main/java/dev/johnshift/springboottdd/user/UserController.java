package dev.johnshift.springboottdd.user;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/** ... */
@RestController
public class UserController {

  @Autowired
  UserRepository repo;

  /** ... */
  @GetMapping("/users")
  public List<User> getUsersHandler() {

    return repo.findAll();

  }
}
