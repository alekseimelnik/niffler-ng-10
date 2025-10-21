package guru.qa.niffler.service.impl;

import guru.qa.niffler.model.auth.UserAuthJson;
import java.util.Optional;
import java.util.UUID;

public interface UserAuthClient {
  UserAuthJson registerUser(UserAuthJson userAuthJson);

  Optional<UserAuthJson> findUserById(UUID id);

  void removeUserById(UUID id);
}
