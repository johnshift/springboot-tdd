package dev.johnshift.springboottdd.user;

import lombok.Data;
import org.springframework.stereotype.Component;

/** ... */
@Data
@Component
public class User {
  private int id;
  private String username;
  private String password;
  private String firstName;
  private String lastName;
  private String description;
}
