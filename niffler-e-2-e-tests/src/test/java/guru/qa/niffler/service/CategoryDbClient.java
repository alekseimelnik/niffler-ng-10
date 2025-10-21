package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.service.impl.CategoryClient;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.UUID;

public class CategoryDbClient implements CategoryClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    try {
      final JdbcTemplate jdbcTemplate = new JdbcTemplate(
          new SingleConnectionDataSource(
              DriverManager.getConnection(
                  CFG.spendJdbcUrl(),
                  "postgres",
                  "secret"
              ),
              true
          )
      );
      final KeyHolder kh = new GeneratedKeyHolder();
      jdbcTemplate.update(conn -> {
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO \"category\" (name, username, archived) " +
                "VALUES (?, ?, ?)",
            Statement.RETURN_GENERATED_KEYS
        );
        ps.setString(1, category.name());
        ps.setString(2, category.username());
        ps.setBoolean(3, category.archived());
        return ps;
      }, kh);
      return new CategoryJson(
          (UUID) kh.getKeys().get("id"),
          category.name(),
          category.username(),
          false
      );
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    if (category.id() == null) {
      throw new IllegalArgumentException("Category ID must not be null for update");
    }
    try {
      final JdbcTemplate jdbcTemplate = new JdbcTemplate(
          new SingleConnectionDataSource(
              DriverManager.getConnection(
                  CFG.spendJdbcUrl(),
                  "postgres",
                  "secret"
              ),
              true
          )
      );
      final KeyHolder kh = new GeneratedKeyHolder();
      jdbcTemplate.update(
          conn -> {
            PreparedStatement ps = conn.prepareStatement(
                "UPDATE \"category\" SET name = ?, username = ?, archived = ? " +
                    "WHERE id = ? RETURNING id, name, username, archived"
            );

            ps.setString(1, category.name());
            ps.setString(2, category.username());
            ps.setBoolean(3, category.archived());
            ps.setObject(4, category.id());
            return ps;
          }, kh);
      return new CategoryJson(
          (UUID) kh.getKeys().get("id"),
          (String) kh.getKeys().get("name"),
          (String) kh.getKeys().get("username"),
          (Boolean) kh.getKeys().get("archived")
      );
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public List<CategoryJson> getAllCategoriesByUsername(String username, boolean excludeArchived) {
    return List.of();
  }
}
