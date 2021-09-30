package dev.johnshift.springboottdd.utils;

import dev.johnshift.springboottdd.user.User;
import dev.johnshift.springboottdd.user.UserDto;
import java.util.Arrays;
import java.util.List;
import org.jeasy.random.EasyRandom;

/** . */
public class Generator {

  /** . */
  public static User generateUser() {
    EasyRandom easyRandom = new EasyRandom();
    User user = easyRandom.nextObject(User.class);
    user.setId(Math.abs(easyRandom.nextLong()));

    return user;
  }

  /** . */
  public static  UserDto generateUserDto() {
    EasyRandom easyRandom = new EasyRandom();
    UserDto dto = easyRandom.nextObject(UserDto.class);
    dto.setId(Math.abs(easyRandom.nextLong()));

    return dto;
  }

  /** . */
  public static List<User> generateUsers() {
    EasyRandom easyRandom = new EasyRandom();
    User user = easyRandom.nextObject(User.class);
    user.setId(Math.abs(easyRandom.nextLong()));

    return Arrays.asList(user);
  }
  
}
