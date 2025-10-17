package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UserDao;
import guru.qa.niffler.data.entity.spend.UserEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.UUID;

public class UserDaoJdbc implements UserDao {

  private static final Config CFG = Config.getInstance();

  @Override
  public UserEntity createUser(UserEntity user) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "INSERT INTO user (username, currency, firstname, surname, photo, photo_small, full_name) " +
              "VALUES ( ?, ?, ?, ?, ?, ?, ?)",
          Statement.RETURN_GENERATED_KEYS
      )) {
        ps.setString(1, user.getUsername());
        ps.setString(2, String.valueOf(user.getCurrency()));
        ps.setString(3, user.getFirstName());
        ps.setString(4, user.getSurname());
        ps.setBytes(5, user.getPhoto());
        ps.setBytes(6, user.getPhotoSmall());
        ps.setString(7, user.getFullName());

        ps.executeUpdate();

        final UUID generatedKey;
        try (ResultSet rs = ps.getGeneratedKeys()) {
          if (rs.next()) {
            generatedKey = rs.getObject("id", UUID.class);
          } else {
            throw new SQLException("Can`t find id in ResultSet");
          }
        }
        user.setId(generatedKey);
        return user;
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findById(UserEntity id) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * FROM user WHERE id = ?"
      )) {
        ps.setObject(1, id);
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            UserEntity ce = new UserEntity();
            ce.setId(rs.getObject("id", UUID.class));
            ce.setUsername(rs.getString("username"));
            ce.setCurrency(ce.getCurrency());
            ce.setFirstName(rs.getString("firstname"));
            ce.setSurname(rs.getString("surname"));
            ce.setPhoto(rs.getBytes("photo"));
            ce.setPhotoSmall(rs.getBytes("photo_small"));
            ce.setFullName(rs.getString("full_name"));
            return Optional.of(ce);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Optional<UserEntity> findByUsername(UserEntity username) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "SELECT * FROM user WHERE username = ?"
      )) {
        ps.setObject(1, username);
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            UserEntity ce = new UserEntity();
            ce.setId(rs.getObject("id", UUID.class));
            ce.setUsername(rs.getString("username"));
            ce.setCurrency(ce.getCurrency());
            ce.setFirstName(rs.getString("firstname"));
            ce.setSurname(rs.getString("surname"));
            ce.setPhoto(rs.getBytes("photo"));
            ce.setPhotoSmall(rs.getBytes("photo_small"));
            ce.setFullName(rs.getString("full_name"));
            return Optional.of(ce);
          } else {
            return Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void delete(UserEntity user) {
    try (Connection connection = Databases.connection(CFG.userdataJdbcUrl())) {
      try (PreparedStatement ps = connection.prepareStatement(
          "DELETE FROM user WHERE id = ?"
      )) {
        ps.setObject(1, user.getId());
        ps.execute();
        try (ResultSet rs = ps.getResultSet()) {
          if (rs.next()) {
            UserEntity ce = new UserEntity();
            ce.setId(rs.getObject("id", UUID.class));
            ce.setUsername(rs.getString("username"));
            ce.setCurrency(ce.getCurrency());
            ce.setFirstName(rs.getString("firstname"));
            ce.setSurname(rs.getString("surname"));
            ce.setPhoto(rs.getBytes("photo"));
            ce.setPhotoSmall(rs.getBytes("photo_small"));
            ce.setFullName(rs.getString("full_name"));
            Optional.of(ce);
          } else {
            Optional.empty();
          }
        }
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }
}
