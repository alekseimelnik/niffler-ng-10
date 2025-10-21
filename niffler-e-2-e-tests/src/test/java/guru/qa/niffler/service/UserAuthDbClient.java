package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.xaTransaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.Databases.XaConsumer;
import guru.qa.niffler.data.Databases.XaFunction;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import guru.qa.niffler.model.auth.Authority;
import guru.qa.niffler.model.auth.UserAuthJson;
import guru.qa.niffler.service.impl.UserAuthClient;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserAuthDbClient implements UserAuthClient {

  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private static final Config CFG = Config.getInstance();

  @Override
  public UserAuthJson registerUser(UserAuthJson userAuthJson) {
    UserAuthEntity userAuthEntity = new UserAuthEntity();
    xaTransaction(
        new XaConsumer(
            connection -> {
              AuthUserDaoJdbc authUserDaoJdbc = new AuthUserDaoJdbc(connection);
              userAuthEntity.setId(authUserDaoJdbc.registerUser(userAuthEntity).getId());
              userAuthEntity.setUsername(userAuthJson.username());
              userAuthEntity.setPassword(pe.encode(userAuthJson.password()));
              userAuthEntity.setEnabled(userAuthJson.enabled());
              userAuthEntity.setAccountNonExpired(userAuthJson.accountNonExpired());
              userAuthEntity.setAccountNonLocked(userAuthJson.accountNonLocked());
              userAuthEntity.setCredentialsNonExpired(userAuthJson.credentialsNonExpired());
            }, CFG.authJdbcUrl()
        ),
        new XaConsumer(
            connection -> {
              AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
              authAuthorityDaoJdbc.setAuthoritiesToUser(Arrays.stream(Authority.values()).map(
                  authority -> {
                    AuthorityEntity authorityEntity = new AuthorityEntity();
                    authorityEntity.setAuthority(authority);
                    authorityEntity.setUser(userAuthEntity);
                    return authorityEntity;
                  }).toArray(AuthorityEntity[]::new)
              );
            }, CFG.authJdbcUrl()
        )
    );
    return UserAuthJson.fromEntity(userAuthEntity);
  }

  @Override
  public Optional<UserAuthJson> findUserById(UUID id) {
    return xaTransaction(
        new XaFunction<>(
            connection -> {
              AuthUserDaoJdbc authUserDaoJdbc = new AuthUserDaoJdbc(connection);
              Optional<UserAuthJson> userAuthJson = authUserDaoJdbc.findUserById(id)
                  .map(UserAuthJson::fromEntity);
              return userAuthJson;
            }, CFG.authJdbcUrl(
        )
    ));
  }


  @Override
  public void removeUserById(UUID id) {
    xaTransaction(
        new XaConsumer(
            connection -> {
              AuthAuthorityDaoJdbc authAuthorityDaoJdbc = new AuthAuthorityDaoJdbc(connection);
              authAuthorityDaoJdbc.deleteAuthoritiesByUserId(
                  id,
                  authAuthorityDaoJdbc.findAuthoritiesByUserId(id).toArray(AuthorityEntity[]::new));
            }, CFG.authJdbcUrl()
        ),
        new XaConsumer(
            connection -> {
              AuthUserDaoJdbc authUserDaoJdbc = new AuthUserDaoJdbc(connection);
              authUserDaoJdbc.removeUserById(id);
            }, CFG.authJdbcUrl()
        )
    );
  }
}
