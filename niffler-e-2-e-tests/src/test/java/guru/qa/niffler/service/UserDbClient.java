package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

public class UserDbClient implements UserClient {

  private static final Config CFG = Config.getInstance();

  private UserDao userDao = new UserDaoJdbc();

  @Override
  public UserJson createUser(UserJson user) {
    UserEntity userEntity = UserEntity.fromJson(user);
    if (userEntity.getId() == null) {
      userEntity.setUsername(userEntity.getUsername());
    }
    return UserJson.fromEntity(
        userDao.createUser(userEntity)
    );
  }

  @Override
  public Optional<UserJson> findById(UserJson id) {
    if (id == null) {
      return Optional.empty();
    } else {
      return Optional.of(
      userDao.findById(UserEntity.fromJson(id))
          .map(userEntity -> UserJson.fromEntity(userEntity)).orElseThrow());
    }
  }

  @Override
  public Optional<UserJson> findByUsername(UserJson username) {
    if (username == null) {
      return Optional.empty();
    } else {
      return Optional.of(
          userDao.findByUsername(UserEntity.fromJson(username))
              .map(userEntity -> UserJson.fromEntity(userEntity)).orElseThrow());
    }
  }

  @Override
  public void delete(UserJson user) {
    userDao.delete(UserEntity.fromJson(user));
  }
}
