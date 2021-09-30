package dev.johnshift.springboottdd.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** ... */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /** ... */
  public List<UserDto> getAllUsers() {

    List<User> users = userRepository.findAll();

    return UserDto.of(users);
  }

  /** . */
  public UserDto createUser(UserDto dto) {
    
    User user = userRepository.save(User.of(dto));
    // todo: throw exception if save failed

    return UserDto.of(user);
  }

  /** . */
  public void deleteUserById(Long id) {

    if (!userRepository.findById(id).isPresent()) {
      throw new UserException(UserException.NOT_FOUND);
    }

    userRepository.deleteById(id);
  }

  /** 
   * Only `bio` field should be updateable.
   */
  public UserDto updateUser(UserDto dto) {

    User user = userRepository.findById(dto.getId())
        .orElseThrow(() -> new UserException(UserException.NOT_FOUND));

    // only update bio
    user.setBio(dto.getBio());

    return UserDto.of(userRepository.save(user));
  }

  /** . */
  public UserDto getUserById(long id) {

    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserException(UserException.NOT_FOUND));

    return UserDto.of(user);
  }
}
