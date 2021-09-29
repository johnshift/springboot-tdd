package dev.johnshift.springboottdd.user;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/** ... */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserRepository userRepository;

  @MockBean
  UserService svc;

  static UserDTO sampleUser;
  static List<UserDTO> sampleUsers;

  @BeforeAll
  static void init() {
    EasyRandom easyRandom = new EasyRandom();

    // need positive value ids
    sampleUser = easyRandom.nextObject(UserDTO.class);
    sampleUser.setId(Math.abs(easyRandom.nextLong()));

    sampleUsers = Arrays.asList(sampleUser);
  }

  @Autowired
  ObjectMapper jsonMapper = new ObjectMapper();

  @Test
  public void handleGetUsers_returnUsers() throws Exception {

    // arrange
    when(svc.getAllUsers()).thenReturn(sampleUsers);

    // act
    mockMvc.perform(get("/users"))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].id", is(sampleUsers.get(0).getId())))
        .andExpect(jsonPath("$.[0].bio", is(sampleUsers.get(0).getBio())))
        .andExpect(jsonPath("$.[0].username", is(sampleUsers.get(0).getUsername())));
  }

  @Test
  public void handleGetUser_existingUser_then_returnUser() throws Exception {
    
    // arrange
    when(svc.getUserById(anyLong())).thenReturn(sampleUser);

    // act
    mockMvc.perform(get("/users/" + sampleUser.getId()))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(sampleUser.getId())))
        .andExpect(jsonPath("$.bio", is(sampleUser.getBio())))
        .andExpect(jsonPath("$.username", is(sampleUser.getUsername())));
  }

  @Test
  public void handleGetUser_nonExistingUser_throwUserException() throws Exception {

    // arrange
    when(svc.getUserById(anyLong())).thenThrow(new UserException(UserException.NOT_FOUND));

    // act
    mockMvc.perform(get("/users/{id}", sampleUser.getId().toString()))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("NOT_FOUND")))
        .andExpect(jsonPath("$.error", is("User not found")))
        .andExpect(jsonPath("$.info", is("some info")))
        .andExpect(jsonPath("$.timestamp").exists());
        
  }

  @Test
  public void handleGetUser_invalidIdType_throwTypeMismatch() throws Exception {

    String statusStr = "BAD_REQUEST";
    String errorStr = "Invalid type mismatch";
    String idPathVariable = "asdf";
    String infoStr = "'id' parameter declared with invalid value of '" + idPathVariable + "'";

    mockMvc.perform(get("/users/asdf"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(statusStr)))
        .andExpect(jsonPath("$.error", is(errorStr)))
        .andExpect(jsonPath("$.info", is(infoStr)))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void handleCreateUser_returnCreatedUser() throws Exception {

    // arrange
    when(svc.createUser(any())).thenReturn(sampleUser);
    String requestBody = jsonMapper.writeValueAsString(sampleUser);

    // act
    mockMvc.perform(
        post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(sampleUser.getId())))
        .andExpect(jsonPath("$.username", is(sampleUser.getUsername())))
        .andExpect(jsonPath("$.bio", is(sampleUser.getBio())));
  }

  @Test
  public void handleCreateUser_noUsername_usernameRequired() throws Exception {

    // arrange
    UserDTO newUser = new UserDTO();
    newUser.setBio("created bio");
    String requestBody = jsonMapper.writeValueAsString(newUser);
    when(svc.createUser(any())).thenReturn(newUser);

    // act
    mockMvc.perform(
        post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.error", is(UserException.USERNAME_REQUIRED)))
        .andExpect(jsonPath("$.info", is("username = null")))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void handleCreateUser_noBio_bioRequired() throws Exception {

    // arrange
    UserDTO newUser = new UserDTO();
    newUser.setUsername("created username");
    String requestBody = jsonMapper.writeValueAsString(newUser);
    when(svc.createUser(any())).thenReturn(newUser);

    // act
    mockMvc.perform(
        post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.error", is(UserException.BIO_REQUIRED)))
        .andExpect(jsonPath("$.info", is("bio = null")))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void handleDeleteUser_invalidIdType_throwTypeMismatch() throws Exception {

    String statusStr = "BAD_REQUEST";
    String errorStr = "Invalid type mismatch";
    String idPathVariable = "asdf";
    String infoStr = "'id' parameter declared with invalid value of '" + idPathVariable + "'";

    mockMvc.perform(delete("/users/asdf"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(statusStr)))
        .andExpect(jsonPath("$.error", is(errorStr)))
        .andExpect(jsonPath("$.info", is(infoStr)))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void handleDeleteUser_existingUser_OK() throws Exception {
    
    // act
    mockMvc.perform(
        delete("/users/{id}", sampleUser.getId().toString()))

        // assert
        .andExpect(status().isOk());
  }

  @Test
  public void handleDeleteUser_nonExistingUser_throwUserException() throws Exception {

    // arrange
    doThrow(new UserException(UserException.NOT_FOUND))
        .when(svc)
        .deleteUserById(anyLong());
    String nonExistingId = "69";

    // act
    mockMvc.perform(
        delete("/users/{id}", nonExistingId))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("NOT_FOUND")))
        .andExpect(jsonPath("$.error", is("User not found")))
        .andExpect(jsonPath("$.info", is("some info")))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void handleUpdateUser_existingUser_onlyUpdateBioField() throws Exception {

    // arrange
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUser.getId());
    updatedUser.setUsername(sampleUser.getUsername());
    updatedUser.setBio("new bio");
    when(svc.updateUser(any())).thenReturn(updatedUser);
    String requestBody = jsonMapper.writeValueAsString(updatedUser);

    // act
    mockMvc.perform(
        put("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isOk())
        // should not change id, username fields
        .andExpect(jsonPath("$.id", is(sampleUser.getId())))
        .andExpect(jsonPath("$.username", is(sampleUser.getUsername())))
        // should only update bio field
        .andExpect(jsonPath("$.bio", is(updatedUser.getBio())));
  }

  @Test
  public void handleUpdateUser_nonExistingUser_throwUserException() throws Exception {

    // arrange
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUser.getId());
    updatedUser.setUsername(sampleUser.getUsername());
    updatedUser.setBio("new bio");
    when(svc.updateUser(any())).thenThrow(new UserException(UserException.NOT_FOUND));
    String requestBody = jsonMapper.writeValueAsString(updatedUser);

    // act
    mockMvc.perform(
        put("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("NOT_FOUND")))
        .andExpect(jsonPath("$.error", is("User not found")))
        .andExpect(jsonPath("$.info", is("some info")))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void handleUpdateUser_noId_idRequired() throws Exception {

    // arrange
    UserDTO updatedUser = new UserDTO();
    updatedUser.setUsername("updated username");
    updatedUser.setBio("updated bio");
    String requestBody = jsonMapper.writeValueAsString(updatedUser);
    when(svc.updateUser(any())).thenReturn(updatedUser);

    // act
    mockMvc.perform(
        put("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.error", is(UserException.ID_REQUIRED)))
        .andExpect(jsonPath("$.info", is("id = null")))
        .andExpect(jsonPath("$.timestamp").exists());

  }
  
  @Test
  public void handleUpdateUser_noUsername_usernameRequired() throws Exception {

    // arrange
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUser.getId());
    updatedUser.setBio(sampleUser.getBio());
    String requestBody = jsonMapper.writeValueAsString(updatedUser);
    when(svc.createUser(any())).thenReturn(updatedUser);

    // act
    mockMvc.perform(
        put("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.error", is(UserException.USERNAME_REQUIRED)))
        .andExpect(jsonPath("$.info", is("username = null")))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void handleUpdateUser_noBio_bioRequired() throws Exception {

    // arrange
    UserDTO updatedUser = new UserDTO();
    updatedUser.setId(sampleUser.getId());
    updatedUser.setUsername(sampleUser.getUsername());
    String requestBody = jsonMapper.writeValueAsString(updatedUser);
    when(svc.createUser(any())).thenReturn(updatedUser);

    // act
    mockMvc.perform(
        post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
        .andExpect(jsonPath("$.error", is(UserException.BIO_REQUIRED)))
        .andExpect(jsonPath("$.info", is("bio = null")))
        .andExpect(jsonPath("$.timestamp").exists());
  }

}
