package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {
  UserAuthEntity registerUser(UserAuthEntity userAuthEntity);

  Optional<UserAuthEntity> findUserById(UUID id);

  void removeUserById(UUID id);
}
