package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.jupiter.annotation.User;
import java.util.Optional;

public interface UserDao {
  UserEntity createUser(UserEntity user);

  Optional<UserEntity> findById(UserEntity id);

  Optional<UserEntity> findByUsername(UserEntity username);

  void delete(UserEntity user);
}
