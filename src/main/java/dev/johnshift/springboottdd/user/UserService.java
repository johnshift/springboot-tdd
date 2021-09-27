package dev.johnshift.springboottdd.user;

import java.util.List;

import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/** ... */
@Service
@RequiredArgsConstructor
public class UserService {

  UserMapper mapper = Mappers.getMapper(UserMapper.class);

  private final UserRepository userRepository;

  /** ... */
  public List<UserDTO> getAllUsers() {

    List<UserEntity> userEntities = userRepository.findAll();

    return mapper.toDTOs(userEntities);
  }

  /** . */
  public UserDTO createUser(UserDTO userDTO) {

    
    UserEntity userEntity = userRepository.save(mapper.toEntity(userDTO));

    return mapper.toDTO(userEntity);
  }

  /** . */
  public UserDTO getUserById(long id) {

    UserEntity userEntity = userRepository.findById(id).orElseThrow();

    return mapper.toDTO(userEntity);


  }
}
