package dev.johnshift.springboottdd.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

/** ... */
@Import(User.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  MockMvc mockMvc;

  @MockBean
  UserService svc;

  // VALIDATION Errors
  // @Test
  // public void Should_ReturnError_When_Username_Empty() throws Exception {}

  // @Test
  // public void Should_ReturnError_When_Bio_Empty() throws Exception {}

  // @Test
  // public void Should_ReturnError_When_Username_AlreadyExists() throws Exception
  // {}

  // @Test
  // public void Should_ReturnError_When_Username_Exceed32() throws Exception {}

  // @Test
  // public void Should_ReturnError_When_Bio_Exceed255() throws Exception {}

  // // NOT FOUND Errors
  // @Test
  // public void GET_User_Should_ReturnError_When_NotFound() throws Exception {}

  // @Test
  // public void PUT_User_Should_ReturnError_When_NotFound() throws Exception {}

  // @Test
  // public void DELETE_User_Should_ReturnError_When_NotFound() throws Exception
  // {}

  // // SUCCESS Requests
  // @Test
  // public void GET_Users_Should_ReturnAllUsers() throws Exception {}

  // @Test
  // public void GET_Should_ReturnUser_Given_UsernamePathVariable() throws
  // Exception {}

  // @Test
  // public void POST_Should_CreateUser_and_ReturnId() throws Exception {}

  // @Test
  // public void PUT_Should_UpdateUser_and_ReturnUpdated() throws Exception {}

  // @Test
  // public void DELETE_Should_DeleteUser_and_ReturnDeleted() throws Exception {}

}
