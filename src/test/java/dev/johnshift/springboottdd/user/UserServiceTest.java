package dev.johnshift.springboottdd.user;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
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

  static UserEntity sampleUserEntity;
  static List<UserEntity> sampleUserEntities;
  static UserDTO sampleUserDTO;

  @BeforeAll
  static void init() {

    EasyRandom easyRandom = new EasyRandom();
    sampleUserEntity = easyRandom.nextObject(UserEntity.class);
    sampleUserDTO = easyRandom.nextObject(UserDTO.class);

    // need positive value ids
    sampleUserEntity.setId(Math.abs(easyRandom.nextLong()));
    sampleUserDTO.setId(Math.abs(easyRandom.nextLong()));

    sampleUserEntities = Arrays.asList(sampleUserEntity);
  }

  @Test
  public void getAllUsers_returnUserDTOs() {

    // arrange
    when(userRepository.findAll()).thenReturn(sampleUserEntities);

    // act
    List<UserDTO> dtos = svc.getAllUsers();

    // assert
    assertEquals(sampleUserEntities.get(0).getId(), dtos.get(0).getId());
    assertEquals(sampleUserEntities.get(0).getBio(), dtos.get(0).getBio());
    assertEquals(sampleUserEntities.get(0).getUsername(), dtos.get(0).getUsername());
  }

  @Test
  public void createUser_returnUserDTO() {

    // arrange
    UserEntity userEntity = new UserEntity();
    Long assignedId = 1L; // assume db assign id = 1
    userEntity.setId(assignedId); 
    userEntity.setBio(sampleUserEntity.getBio());
    userEntity.setUsername(sampleUserEntity.getUsername());
    when(userRepository.save(any())).thenReturn(userEntity);

    // act
    UserDTO dto = svc.createUser(sampleUserDTO);

    // assert
    assertNotNull(dto.getId());
    assertEquals(userEntity.getId(), dto.getId());
    assertEquals(userEntity.getBio(), dto.getBio());
    assertEquals(userEntity.getUsername(), dto.getUsername());
  }

  @Test
  public void deleteUserById_OK() {

    // arrange
    when(userRepository.findById(sampleUserEntity.getId())).thenReturn(Optional.of(sampleUserEntity));

    // act
    // assert
    assertDoesNotThrow(() -> svc.deleteUserById(sampleUserEntity.getId()));
  }

  @Test
  public void given_userNotFound_when_deleteUserById_then_throwUserException() {

    // arrange
    long id = sampleUserEntity.getId();
    // assume db can't find the id
    when(userRepository.findById(id)).thenReturn(Optional.empty());

    // // act
    // // assert
    assertThrows(UserException.class, () -> svc.deleteUserById(id));
  }

  @Test
  public void should_Succeed_When_UpdateUser() {

    // arrange
    long id = sampleUserEntity.getId();
    when(userRepository.findById(id)).thenReturn(Optional.of(sampleUserEntity));
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUserEntity.getId());
    updatedUser.setUsername(sampleUserEntity.getUsername());
    updatedUser.setBio("new bio");

    // act
    UserDTO dto = svc.updateUser(updatedUser);
    
    // assert
    assertEquals(updatedUser.getId(), dto.getId());
    assertEquals(updatedUser.getUsername(), dto.getUsername());
    assertEquals(updatedUser.getBio(), dto.getBio());
  }
  
  @Test
  public void given_userNotFound_when_updateUser_then_throwUserException() {
    
    // arrange
    long id = sampleUserEntity.getId();
    when(userRepository.findById(id)).thenReturn(Optional.empty());
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUserEntity.getId());
    updatedUser.setUsername(sampleUserEntity.getUsername());
    updatedUser.setBio("new bio");

    // act
    // assert
    assertThrows(UserException.class, () -> svc.updateUser(updatedUser));
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
    long id = sampleUserEntity.getId();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUserEntity));

    // act
    boolean exists = svc.checkUserExists(id);

    // assert
    assertEquals(true, exists);
  }

  @Test
  public void given_userExists_when_getUserById_then_returnUser() {

    // arrange
    long id = sampleUserEntity.getId();
    when(userRepository.findById(anyLong())).thenReturn(Optional.of(sampleUserEntity));

    // act
    UserDTO dto = svc.getUserById(id);

    // assert
    assertEquals(sampleUserEntity.getId(), dto.getId());
    assertEquals(sampleUserEntity.getBio(), dto.getBio());
    assertEquals(sampleUserEntity.getUsername(), dto.getUsername());
  }

  @Test
  public void given_userNotFound_when_getUserById_then_throwUserException() {
  
    assertThrows(UserException.class, () -> svc.getUserById(99999L));
  }
}
