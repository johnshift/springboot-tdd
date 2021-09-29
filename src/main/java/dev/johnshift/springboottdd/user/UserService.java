package dev.johnshift.springboottdd.user;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

/** ... */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

  private final UserRepository userRepository;

  /** ... */
  public List<UserDTO> getAllUsers() {

    List<UserEntity> userEntities = userRepository.findAll();

    return mapper.toDTOs(userEntities);
  }

  /** . */
  public UserDTO createUser(UserDTO userDTO) {
    
    UserEntity userEntity = userRepository.save(mapper.toEntity(userDTO));
    // todo: throw exception if save failed

    return mapper.toDTO(userEntity);
  }

  /** . */
  public void deleteUserById(Long id) {

    if (!checkUserExists(id)) {
      throw new UserException(UserException.NOT_FOUND);
    }

    userRepository.deleteById(id);
  }

  /** 
   * Only `bio` field should be updateable.
   */
  public UserDTO updateUser(UserDTO userDTO) {

    Long id = userDTO.getId();

    if (!checkUserExists(id)) {
      throw new UserException(UserException.NOT_FOUND);
    }

    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new UserException(UserException.NOT_FOUND));

    userEntity.setBio(userDTO.getBio());
    userRepository.save(userEntity);

    return mapper.toDTO(userEntity);
  }

  /** . */
  public UserDTO getUserById(long id) {

    UserEntity userEntity = userRepository.findById(id)
        .orElseThrow(() -> new UserException(UserException.NOT_FOUND));

    return mapper.toDTO(userEntity);
  }

  /** . */
  public boolean checkUserExists(long id) {
    return userRepository.findById(id).isPresent();
  }
}
