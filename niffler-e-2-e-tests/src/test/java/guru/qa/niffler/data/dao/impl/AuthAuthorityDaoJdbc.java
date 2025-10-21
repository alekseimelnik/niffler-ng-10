package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private final Connection connection;

  public AuthAuthorityDaoJdbc(Connection connection) {
    this.connection = connection;
  }

  @Override
  public List<AuthorityEntity> setAuthoritiesToUser(AuthorityEntity... authorities) {
    try (PreparedStatement ps = connection.prepareStatement(
        "INSERT INTO authority (authority, user_id) " +
            "VALUES (?, ?)",
        Statement.RETURN_GENERATED_KEYS
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setString(1, authority.getAuthority().name());
        ps.setObject(2, authority.getUser().getId());
        ps.addBatch();
      }
      ps.executeBatch();
      try (ResultSet rs = ps.getGeneratedKeys()) {
        List<AuthorityEntity> setAuthorities = new ArrayList<>();
        while (rs.next()) {
          UUID generatedKey = rs.getObject("id", UUID.class);
          for (AuthorityEntity authority : authorities) {
            if (authority.getId() == null) {
              authority.setId(generatedKey);
              setAuthorities.add(authority);
              break;
            }
          }
        }
        return setAuthorities;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
    try (PreparedStatement ps = connection.prepareStatement(
        "SELECT * FROM authority WHERE user_id = ?"
    )) {
      ps.setObject(1, userId);
      ps.execute();
      try (ResultSet rs = ps.executeQuery()) {
        List<AuthorityEntity> authorities = new ArrayList<>();
        while (rs.next()) {
          AuthorityEntity authority = new AuthorityEntity();
          authority.setId(rs.getObject("id", UUID.class));
          authorities.add(authority);
        }
        return authorities;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteAuthoritiesByUserId(UUID userId, AuthorityEntity... authorities) {
    try (PreparedStatement ps = connection.prepareStatement(
        "DELETE FROM authority WHERE user_id = ? AND authority = ?"
    )) {
      for (AuthorityEntity authority : authorities) {
        ps.setObject(1, userId);
        ps.setString(2, authority.getAuthority().name());
        ps.addBatch();
      }
      ps.executeBatch();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
