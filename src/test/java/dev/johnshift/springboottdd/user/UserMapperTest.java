package dev.johnshift.springboottdd.user;

import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/** . */
@ExtendWith(SpringExtension.class)
public class UserMapperTest {
  
  UserMapper mapper = Mappers.getMapper(UserMapper.class);

  @Test
  public void shouldMapUserTdoIntoUserEntity() {

    UserDTO dto = new UserDTO();
    dto.setId(1L);
    dto.setBio("bio");
    dto.setUsername("username");

    UserEntity entity = mapper.toEntity(dto);

    assertEquals(dto.getId(), entity.getId());
    assertEquals(dto.getBio(), entity.getBio());
    assertEquals(dto.getUsername(), entity.getUsername());
  }

  @Test
  public void shouldMapUserEntityIntoUserDTO() {

    UserEntity entity = new UserEntity();
    entity.setId(1L);
    entity.setBio("bio");
    entity.setUsername("username");

    UserDTO dto = mapper.toDTO(entity);

    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getBio(), dto.getBio());
    assertEquals(entity.getUsername(), dto.getUsername());
  }

  @Test
  public void shouldMapUsersDTOIntoUserEntities() {

    UserEntity entity = new UserEntity();
    entity.setId(1L);
    entity.setBio("bio");
    entity.setUsername("username");

    List<UserEntity> entities = new ArrayList<>();
    entities.add(entity);

    List<UserDTO> dtos = mapper.toDTOs(entities);

    assertEquals(entities.get(0).getId(), dtos.get(0).getId());
    assertEquals(entities.get(0).getBio(), dtos.get(0).getBio());
    assertEquals(entities.get(0).getUsername(), dtos.get(0).getUsername());
  }

  @Test
  public void shouldMapNullUsersDTO() {

    UserDTO dto = null;

    UserEntity entity = mapper.toEntity(dto);

    assertEquals(null, entity);
    assertSame(entity, dto);
  }

  @Test
  public void shouldMapNullUsersDTOs() {

    List<UserEntity> entities = null;

    List<UserDTO> dtos = mapper.toDTOs(entities);

    assertEquals(null, dtos);
  }

  @Test
  public void shouldMapNullEntityDTO() {

    UserEntity entity = null;

    UserDTO dto = mapper.toDTO(entity);

    assertEquals(null, dto);
  }

}
