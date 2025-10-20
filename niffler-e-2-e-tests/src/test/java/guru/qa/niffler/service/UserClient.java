package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import java.util.Optional;
import java.util.UUID;

public interface UserClient {
  UserJson createUser(UserJson user);

  Optional<UserJson> findById(UUID id);

  Optional<UserJson> findByUsername(String username);

  void delete(UserJson user);

}
