package dev.johnshift.springboottdd.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import dev.johnshift.springboottdd.utils.Generator;
import java.util.List;
import org.junit.jupiter.api.Test;

/** . */
public class UserDTOTest {

  static final User user = Generator.generateUser();
  static final List<User> users = Generator.generateUsers();

  @Test
  public void user_to_userDto_OK() {

    UserDto dto = UserDto.of(user);

    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getUsername(), dto.getUsername());
    assertEquals(user.getBio(), dto.getBio());
  }

  @Test
  public void userList_to_userDtoList_OK() {

    List<UserDto> dtos = UserDto.of(users);

    assertEquals(users.get(0).getId(), dtos.get(0).getId());
    assertEquals(users.get(0).getUsername(), dtos.get(0).getUsername());
    assertEquals(users.get(0).getBio(), dtos.get(0).getBio());

  }
}
