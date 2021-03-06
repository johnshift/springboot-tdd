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
import dev.johnshift.springboottdd.exceptions.AppExceptionHandler;
import dev.johnshift.springboottdd.utils.Generator;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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

  static final User user = Generator.generateUser();
  static final List<User> users = Generator.generateUsers();
  static final UserDto userDto = Generator.generateUserDto();

  @Autowired
  ObjectMapper jsonMapper = new ObjectMapper();

  @Test
  public void get_users_OK() throws Exception {

    // arrange
    // when(svc.getAllUsers()).thenReturn(users);
    when(svc.getAllUsers()).thenAnswer(i -> users);

    // act
    mockMvc.perform(get("/users"))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].id", is(users.get(0).getId())))
        .andExpect(jsonPath("$.[0].bio", is(users.get(0).getBio())))
        .andExpect(jsonPath("$.[0].username", is(users.get(0).getUsername())));
  }

  @Test
  public void get_user_OK() throws Exception {
    
    // arrange
    when(svc.getUserById(user.getId())).thenAnswer(i -> userDto);

    // act
    mockMvc.perform(get("/users/" + user.getId()))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.bio", is(user.getBio())))
        .andExpect(jsonPath("$.username", is(user.getUsername())));
  }

  @Test
  public void get_nonExistingUser_throw_NotFound() throws Exception {

    // arrange
    when(svc.getUserById(anyLong())).thenThrow(new UserException(UserException.NOT_FOUND));
    String info = "No user found with id = " + user.getId();

    // act
    mockMvc.perform(get("/users/{id}", user.getId().toString()))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
        .andExpect(jsonPath("$.error", is(UserException.NOT_FOUND)))
        .andExpect(jsonPath("$.info", is(info)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void get_user_byInvalidId_throw_BadRequest() throws Exception {

    String infoStr = "'id' parameter declared with invalid value of 'asdf'";

    mockMvc.perform(get("/users/{id}", "asdf"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
        .andExpect(jsonPath("$.error", is(AppExceptionHandler.INVALID_TYPE)))
        .andExpect(jsonPath("$.info", is(infoStr)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void post_user_OK() throws Exception {

    // arrange
    when(svc.createUser(any(UserDto.class))).thenAnswer(i -> userDto);
    String requestBody = jsonMapper.writeValueAsString(userDto);

    // act
    mockMvc.perform(
        post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.bio", is(user.getBio())));
  }

  @Test
  public void post_user_noUsername_throw_UsernameRequired() throws Exception {

    // arrange
    UserDto newUser = new UserDto();
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
        .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
        .andExpect(jsonPath("$.error", is(UserException.USERNAME_REQUIRED)))
        .andExpect(jsonPath("$.info", is("username = null")))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void post_user_noBio_throw_BioRequired() throws Exception {

    // arrange
    UserDto newUser = new UserDto();
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
        .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
        .andExpect(jsonPath("$.error", is(UserException.BIO_REQUIRED)))
        .andExpect(jsonPath("$.info", is("bio = null")))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void delete_user_invalidIdType_throw_BadRequest() throws Exception {

    String infoStr = "'id' parameter declared with invalid value of 'asdf'";

    mockMvc.perform(delete("/users/asdf"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
        .andExpect(jsonPath("$.error", is(AppExceptionHandler.INVALID_TYPE)))
        .andExpect(jsonPath("$.info", is(infoStr)))
        .andExpect(jsonPath("$.timestamp").exists());

  }

  @Test
  public void delete_user_existingUser_OK() throws Exception {
    
    // act
    mockMvc.perform(
        delete("/users/{id}", user.getId().toString()))

        // assert
        .andExpect(status().isOk());
  }

  @Test
  public void delete_nonExistingUser_throw_NotFound() throws Exception {

    // arrange
    doThrow(new UserException(UserException.NOT_FOUND))
        .when(svc)
        .deleteUserById(anyLong());
    String nonExistingId = "69";
    String info = "No user found with id = " + nonExistingId;

    // act
    mockMvc.perform(
        delete("/users/{id}", nonExistingId))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
        .andExpect(jsonPath("$.error", is(UserException.NOT_FOUND)))
        .andExpect(jsonPath("$.info", is(info)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void update_user_OK() throws Exception {

    // arrange
    UserDto updatedUser = new UserDto();
    updatedUser.setId(user.getId());
    updatedUser.setUsername(user.getUsername());
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
        .andExpect(jsonPath("$.id", is(user.getId())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        // should only update bio field
        .andExpect(jsonPath("$.bio", is(updatedUser.getBio())));
  }

  @Test
  public void update_nonExistingUser_throw_NotFound() throws Exception {

    // arrange
    UserDto updatedUser = new UserDto();
    updatedUser.setId(user.getId());
    updatedUser.setUsername(user.getUsername());
    updatedUser.setBio("new bio");
    when(svc.updateUser(any())).thenThrow(new UserException(UserException.NOT_FOUND));
    String requestBody = jsonMapper.writeValueAsString(updatedUser);
    String info = "Username '" + updatedUser.getUsername() + "' does not exists";

    // act
    mockMvc.perform(
        put("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestBody))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
        .andExpect(jsonPath("$.error", is(UserException.NOT_FOUND)))
        .andExpect(jsonPath("$.info", is(info)))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  public void update_user_noId_throw_IdRequired() throws Exception {

    // arrange
    UserDto updatedUser = new UserDto();
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
  public void update_user_noUsername_throw_UsernameRequired() throws Exception {

    // arrange
    UserDto updatedUser = new UserDto();
    updatedUser.setId(user.getId());
    updatedUser.setBio(user.getBio());
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
  public void update_user_noBio_throw_BioRequired() throws Exception {

    // arrange
    UserDto updatedUser = new UserDto();
    updatedUser.setId(user.getId());
    updatedUser.setUsername(user.getUsername());
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
