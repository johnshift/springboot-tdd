package dev.johnshift.springboottdd.user;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import dev.johnshift.springboottdd.exceptions.NotFoundException;
import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** . */
@ExtendWith(MockitoExtension.class)
@ExtendWith(RandomBeansExtension.class)
public class UserServiceTest {

  @Mock
  UserRepository userRepository;

  @InjectMocks
  UserService svc;

  @Random(excludes = {"id"})
  UserDTO createUserRequest;

  @Random
  UserEntity userEntity;

  @Random
  UserDTO userDTO;

  @Random(type = UserEntity.class, size = 5)
  List<UserEntity> userEntities;

  @Test
  public void return_userDTOs_when_getAllUsers() {

    // arrange
    when(userRepository.findAll()).thenReturn(userEntities);

    // act
    List<UserDTO> dtos = svc.getAllUsers();

    // assert
    assertEquals(userEntities.get(0).getId(), dtos.get(0).getId());
    assertEquals(userEntities.get(0).getBio(), dtos.get(0).getBio());
    assertEquals(userEntities.get(0).getUsername(), dtos.get(0).getUsername());
  }

  @Test
  public void return_userDTO_when_createUser() {

    // arrange
    UserEntity userEntity = new UserEntity();
    userEntity.setId(1L); // assume db assign id = 1
    userEntity.setBio(createUserRequest.getBio());
    userEntity.setUsername(createUserRequest.getUsername());
    when(userRepository.save(any())).thenReturn(userEntity);

    // act
    UserDTO dto = svc.createUser(createUserRequest);

    // assert
    assertNotNull(dto.getId());
    assertEquals(createUserRequest.getBio(), dto.getBio());
    assertEquals(createUserRequest.getUsername(), dto.getUsername());
  }

  @Test
  public void succeed_when_deleteUserById() {

    // arrange
    when(userRepository.findById(userEntity.getId())).thenReturn(Optional.of(userEntity));

    // act
    // assert
    assertDoesNotThrow(() -> svc.deleteUserById(userEntity.getId()));
  }

  @Test
  public void given_userNotFound_when_deleteUserById_then_throwNotFoundException() {

    // arrange
    long id = userEntity.getId();
    // assume db can't find the id
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    // // act
    // // assert
    assertThrows(NotFoundException.class, () -> svc.deleteUserById(id));
  }

  @Test
  public void should_Succeed_When_UpdateUser() {

    // arrange
    long id = userEntity.getId();
    when(userRepository.findById(id)).thenReturn(Optional.of(userEntity));
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(userEntity.getId());
    updatedUser.setUsername(userEntity.getUsername());
    updatedUser.setBio("new bio");

    // act
    UserDTO dto = svc.updateUser(updatedUser);
    
    // assert
    assertEquals(updatedUser.getId(), dto.getId());
    assertEquals(updatedUser.getUsername(), dto.getUsername());
    assertEquals(updatedUser.getBio(), dto.getBio());
  }
  
  @Test
  public void given_userNotFound_when_updateUser_then_throwNotFoundException() {
    
    // arrange
    long id = userEntity.getId();
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(userEntity.getId());
    updatedUser.setUsername(userEntity.getUsername());
    updatedUser.setBio("new bio");

    // act
    // assert
    assertThrows(NotFoundException.class, () -> svc.updateUser(updatedUser));
  }

  @Test
  public void given_userNotFound_when_checkUserExists_then_returnFalse() {
    
    // arrange
    long id = 69L;
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    // act
    boolean exists = svc.checkUserExists(id);

    // assert
    assertEquals(false, exists);
  }

  @Test
  public void given_userExists_when_checkUserExists_then_returnTrue() {
    
    // arrange
    long id = userEntity.getId();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

    // act
    boolean exists = svc.checkUserExists(id);

    // assert
    assertEquals(true, exists);
  }

  @Test
  public void given_userExists_when_getUserById_then_returnUser() {

    // arrange
    long id = userEntity.getId();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(userEntity));

    // act
    UserDTO dto = svc.getUserById(id);

    // assert
    assertEquals(userEntity.getId(), dto.getId());
    assertEquals(userEntity.getBio(), dto.getBio());
    assertEquals(userEntity.getUsername(), dto.getUsername());
  }

  @Test
  public void given_userNotFound_when_getUserById_then_throwNotFoundException() {
    // arrange
    long id = 69L;
    when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

    // act
    // assert
    assertThrows(NotFoundException.class, () -> svc.getUserById(id));
  }
}
