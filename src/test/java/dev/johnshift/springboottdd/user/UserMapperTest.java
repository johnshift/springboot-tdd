package dev.johnshift.springboottdd.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.mapstruct.factory.Mappers;

/** . */
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

}
