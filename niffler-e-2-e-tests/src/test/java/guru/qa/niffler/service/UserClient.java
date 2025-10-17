package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import java.util.List;
import java.util.Optional;

public interface UserClient {
  UserJson createUser(UserJson user);

  Optional<UserJson> findById(UserJson id);

  Optional<UserJson> findByUsername(UserJson username);

  void delete(UserJson user);

}
