package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.UserAuthEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

  private final Connection connection;

  public AuthUserDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public UserAuthEntity registerUser(UserAuthEntity userAuthEntity) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
            + "VALUES (?, ?, ?, ?, ?, ?)",
        PreparedStatement.RETURN_GENERATED_KEYS)) {
      ps.setString(1, userAuthEntity.getUsername());
      ps.setString(2, userAuthEntity.getPassword());
      ps.setBoolean(3, userAuthEntity.getEnabled());
      ps.setBoolean(4, userAuthEntity.getAccountNonExpired());
      ps.setBoolean(5, userAuthEntity.getAccountNonLocked());
      ps.setBoolean(6, userAuthEntity.getCredentialsNonExpired());
      ps.executeUpdate();
      final UUID generatedKey;
      try (ResultSet rs = ps.getGeneratedKeys()) {
        if (rs.next()) {
          generatedKey = rs.getObject("id", UUID.class);
        } else {
          throw new SQLException("Can`t find id in ResultSet");
        }
      }
      userAuthEntity.setId(generatedKey);
      return userAuthEntity;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserAuthEntity> findUserById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          UserAuthEntity user = new UserAuthEntity();
          user.setId(rs.getObject("id", UUID.class));
          user.setUsername(rs.getString("username"));
          user.setPassword(rs.getString("password"));
          user.setEnabled(rs.getBoolean("enabled"));
          user.setAccountNonExpired(rs.getBoolean("account_non_expired"));
          user.setAccountNonLocked(rs.getBoolean("account_non_locked"));
          user.setCredentialsNonExpired(rs.getBoolean("credentials_non_expired"));
          return Optional.of(user);
        } else {
          return Optional.empty();
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void removeUserById(UUID id) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM \"user\" WHERE id = ?"
    )) {
      ps.setObject(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
