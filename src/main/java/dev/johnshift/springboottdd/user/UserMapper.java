package dev.johnshift.springboottdd.user;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/** ... */
@Mapper
public interface UserMapper {
  
  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "bio", target = "bio")
  UserDTO toDTO(UserEntity userEntity);
  
  UserEntity toEntity(UserDTO userDTO);

  List<UserDTO> toDTOs(List<UserEntity> userEntities);
}
