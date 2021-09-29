package dev.johnshift.springboottdd.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

/** ... */
@Getter
@Setter
public class UserDTO {

  @PositiveOrZero
  @NotNull(message = UserException.ID_REQUIRED, groups = OnUpdate.class)
  private Long id;

  @NotEmpty(message = UserException.USERNAME_REQUIRED)
  private String username;

  @NotEmpty(message = UserException.BIO_REQUIRED)
  private String bio;
  
}
