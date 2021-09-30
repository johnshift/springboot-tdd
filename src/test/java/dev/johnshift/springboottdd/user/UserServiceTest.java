package dev.johnshift.springboottdd.user;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import dev.johnshift.springboottdd.utils.Generator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** . */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService svc;

  static final User user = Generator.generateUser();
  static final List<User> users = Generator.generateUsers();
  static final UserDto userDto = Generator.generateUserDto();

  @Test
  public void getAllUsers_OK() {

    // arrange
    when(userRepository.findAll()).thenReturn(users);

    // act
    List<UserDto> dtos = svc.getAllUsers();

    // assert
    assertEquals(users.get(0).getId(), dtos.get(0).getId());
    assertEquals(users.get(0).getBio(), dtos.get(0).getBio());
    assertEquals(users.get(0).getUsername(), dtos.get(0).getUsername());
  }

  @Test
  public void createUser_OK() {

    // arrange
    User userEntity = new User();
    Long assignedId = 1L; // assume db assign id = 1
    userEntity.setId(assignedId); 
    userEntity.setBio(user.getBio());
    userEntity.setUsername(user.getUsername());
    when(userRepository.save(any())).thenReturn(userEntity);

    // act
    UserDto dto = svc.createUser(userDto);

    // assert
    assertNotNull(dto.getId());
    assertEquals(userEntity.getId(), dto.getId());
    assertEquals(userEntity.getBio(), dto.getBio());
    assertEquals(userEntity.getUsername(), dto.getUsername());
  }

  @Test
  public void deleteUserById_OK() {

    // arrange
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    // act
    // assert
    assertDoesNotThrow(() -> svc.deleteUserById(user.getId()));
  }

  @Test
  public void deleteUserById_userNotFound_throw_UserException() {

    // arrange
    long id = user.getId();
    // assume db can't find the id
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    // // act
    // // assert
    assertThrows(UserException.class, () -> svc.deleteUserById(id));
  }

  @Test
  public void updateUser_OK() throws Exception {

    // arrange
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userRepository.save(any())).thenReturn(user);
    UserDto dtoReq = UserDto.of(user);

    // // act
    UserDto dto = svc.updateUser(dtoReq);
    
    // // assert
    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getUsername(), dto.getUsername());
    assertEquals(user.getBio(), dto.getBio());
  }
  
  @Test
  public void given_userNotFound_when_updateUser_then_throwUserException() {
    
    // arrange
    long id = user.getId();
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    UserDto updatedUser = new UserDto();
    updatedUser.setId(user.getId());
    updatedUser.setUsername(user.getUsername());
    updatedUser.setBio("new bio");

    // act
    // assert
    assertThrows(UserException.class, () -> svc.updateUser(updatedUser));
  }

  @Test
  public void given_userExists_when_getUserById_then_returnUser() {

    // arrange
    long id = user.getId();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

    // act
    UserDto dto = svc.getUserById(id);

    // assert
    assertEquals(user.getId(), dto.getId());
    assertEquals(user.getBio(), dto.getBio());
    assertEquals(user.getUsername(), dto.getUsername());
  }

  @Test
  public void given_userNotFound_when_getUserById_then_throwUserException() {
  
    assertThrows(UserException.class, () -> svc.getUserById(99999L));
  }
}
