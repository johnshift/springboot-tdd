package dev.johnshift.springboottdd.user;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.github.glytching.junit.extension.random.Random;
import io.github.glytching.junit.extension.random.RandomBeansExtension;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

/** ... */
@ExtendWith(SpringExtension.class)
@ExtendWith(RandomBeansExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserRepository userRepository;

  // @Autowired
  @MockBean
  UserService svc;

  @Random
  UserDTO userDTO;

  @Random(type = UserDTO.class, size = 1)
  List<UserDTO> userDTOs;

  @Test
  public void should_returnUsers_when_handleGetUsers() throws Exception {

    // arrange
    when(svc.getAllUsers()).thenReturn(userDTOs);

    // act
    mockMvc.perform(get("/users"))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()", is(1)))
        .andExpect(jsonPath("$.[0].id", is(userDTOs.get(0).getId())))
        .andExpect(jsonPath("$.[0].bio", is(userDTOs.get(0).getBio())))
        .andExpect(jsonPath("$.[0].username", is(userDTOs.get(0).getUsername())));
  }

  @Test
  public void given_existingUser_when_handleGetUser_then_returnUser() throws Exception {
    
    // arrange
    when(svc.getUserById(anyLong())).thenReturn(userDTO);

    // act
    mockMvc.perform(get("/users/" + userDTO.getId()))

        // assert
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(userDTO.getId())))
        .andExpect(jsonPath("$.bio", is(userDTO.getBio())))
        .andExpect(jsonPath("$.username", is(userDTO.getUsername())));
  }

  @Test
  public void given_nonExistingUser_when_handleGetUser_then_throwException() throws Exception {

    // arrange
    when(svc.getUserById(anyLong())).thenThrow(new UserException(UserException.NOT_FOUND));

    // act
    mockMvc.perform(get("/users/{id}", userDTO.getId().toString()))

        // assert
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.status", is("NOT_FOUND")))
        .andExpect(jsonPath("$.error", is("User not found")))
        .andExpect(jsonPath("$.info", is("some info")));
        
  }

}
