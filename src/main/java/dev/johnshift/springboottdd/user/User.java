package dev.johnshift.springboottdd.user;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ... */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "username")
  private String username;

  @Column(name = "bio")
  private String bio;

  // mapper dto -> user
  public static User of(UserDto dto) {
    return new User(dto.getId(), dto.getUsername(), dto.getBio());
  }

  /** maps dto list -> user list. */
  public static List<User> of(List<UserDto> dtos) {
    List<User> users = new ArrayList<>();

    for (UserDto dto : dtos) {
      users.add(new User(dto.getId(), dto.getUsername(), dto.getBio()));
    }

    return users;
  }
}