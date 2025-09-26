package guru.qa.niffler.service;


import guru.qa.niffler.api.CategoryApi;
import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CategoryApiClient implements CategoryClient{

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final CategoryApi categoryApi = retrofit.create(CategoryApi.class);

  @Override
  public CategoryJson createCategory(CategoryJson category) {
      final Response<CategoryJson> response;
      try {
          response = categoryApi.createCategory(category)
                  .execute();
      } catch (IOException e) {
          throw new AssertionError(e);
      }
      assertEquals(200, response.code());
      return response.body();
  }

  @Override
  public Optional<CategoryJson> findCategoryByNameAndUsername(String categoryName, String username) {
    throw new UnsupportedOperationException("Not implemented :(");
  }
}
