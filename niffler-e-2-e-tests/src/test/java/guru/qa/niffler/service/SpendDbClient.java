package guru.qa.niffler.service;

import static guru.qa.niffler.data.Databases.transaction;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.impl.SpendClient;

public class SpendDbClient implements SpendClient {

  private static final Config CFG = Config.getInstance();

  @Override
  public SpendJson createSpend(SpendJson spend) {
    return transaction(connection -> {
          SpendEntity spendEntity = SpendEntity.fromJson(spend);
          if (spendEntity.getCategory().getId() == null) {
            CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                .create(spendEntity.getCategory());
            spendEntity.setCategory(categoryEntity);
          }
          return SpendJson.fromEntity(
              new SpendDaoJdbc(connection).create(spendEntity)
          );
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public CategoryJson createCategory(CategoryJson category) {
    return transaction(connection -> {
          return CategoryJson.fromEntity(
              new CategoryDaoJdbc(connection).create(
                  CategoryEntity.fromJson(category)
              )
          );
        },
        CFG.spendJdbcUrl()
    );
  }

  @Override
  public CategoryJson updateCategory(CategoryJson category) {
    return transaction(connection -> {
          return CategoryJson.fromEntity(
              new CategoryDaoJdbc(connection).update(
                  CategoryEntity.fromJson(category)
              )
          );
        },
        CFG.spendJdbcUrl()
    );
  }
}
