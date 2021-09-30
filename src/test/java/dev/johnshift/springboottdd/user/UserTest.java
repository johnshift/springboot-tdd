package dev.johnshift.springboottdd.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

/** . */
public class UserTest {
  
  @Test
  public void user_allArgsConstructor_OK() {
    
    Long id = 0L;
    String username = "username";
    String bio = "bio";

    User user = new User(id, username, bio);
  
    assertEquals(id, user.getId());
    assertEquals(username, user.getUsername());
    assertEquals(bio, user.getBio());
  }

  @Test
  public void user_of_OK() {

    Long id = 0L;
    String username = "username";
    String bio = "bio";
    UserDto dto = new UserDto(id, username, bio);

    User user = User.of(dto);

    assertEquals(id, user.getId());
    assertEquals(username, user.getUsername());
    assertEquals(bio, user.getBio());
  }

  @Test
  public void user_of_list_OK() {

    Long id = 0L;
    String username = "username";
    String bio = "bio";
    UserDto dto = new UserDto(id, username, bio);
    List<UserDto> dtos = Arrays.asList(dto);

    List<User> users = User.of(dtos);

    assertEquals(id, users.get(0).getId());
    assertEquals(username, users.get(0).getUsername());
    assertEquals(bio, users.get(0).getBio());
  }

}
