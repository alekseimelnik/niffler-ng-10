package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.transaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;
import java.util.Optional;
import java.util.UUID;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserJson createUser(UserJson user) {
    return transaction(connection -> {
          UserEntity userEntity = UserEntity.fromJson(user);
          return UserJson.fromEntity(
              new UserDaoJdbc(connection).createUser(userEntity)
          );
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public Optional<UserJson> findById(UUID id) {
    return transaction(connection -> {
          return new UserDaoJdbc(connection).findById(id)
              .map(UserJson::fromEntity);
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public Optional<UserJson> findByUsername(String username) {
    return transaction(connection -> {
          return new UserDaoJdbc(connection).findByUsername(username)
              .map(UserJson::fromEntity);
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public void delete(UserJson user) {
    transaction(connection -> {
          new UserDaoJdbc(connection).delete(UserEntity.fromJson(user));
        },
        CFG.spendJdbcUrl()
    );
  }
}
