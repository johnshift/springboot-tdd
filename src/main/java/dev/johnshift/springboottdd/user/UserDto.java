package dev.johnshift.springboottdd.user;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** ... */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

  @PositiveOrZero
  @NotNull(message = UserException.ID_REQUIRED, groups = OnUpdate.class)
  private Long id;

  @NotEmpty(message = UserException.USERNAME_REQUIRED)
  private String username;
  
  @NotEmpty(message = UserException.BIO_REQUIRED)
  private String bio;

  // mapper user -> dto
  public static UserDto of(User user) {

    return new UserDto(user.getId(), user.getUsername(), user.getBio());
  }

  /** maps user list -> dto list. */
  public static List<UserDto> of(List<User> users) {
    List<UserDto> dtos = new ArrayList<>();

    for (User user : users) {
      dtos.add(new UserDto(user.getId(), user.getUsername(), user.getBio()));
    }

    return dtos;
  }
  
}
