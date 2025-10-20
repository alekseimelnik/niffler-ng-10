package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.dao.impl.UserDaoJdbc;
import guru.qa.niffler.data.entity.spend.UserEntity;
import guru.qa.niffler.model.UserJson;
import java.util.Optional;
import java.util.UUID;

public class UserDbClient implements UserClient {

  private UserDao userDao = new UserDaoJdbc();

  @Override
  public UserJson createUser(UserJson user) {
    UserEntity userEntity = UserEntity.fromJson(user);
    return UserJson.fromEntity(
        userDao.createUser(userEntity)
    );
  }

  @Override
  public Optional<UserJson> findById(UUID id) {
    return userDao.findById(id)
        .map(UserJson::fromEntity);
  }

  @Override
  public Optional<UserJson> findByUsername(String username) {
    return userDao.findByUsername(username)
        .map(UserJson::fromEntity);
  }

  @Override
  public void delete(UserJson user) {
    userDao.delete(UserEntity.fromJson(user));
  }
}
