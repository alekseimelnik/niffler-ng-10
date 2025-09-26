package guru.qa.niffler.service;


import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient{

  private static final Config CFG = Config.getInstance();

  private final Retrofit retrofit = new Retrofit.Builder()
      .baseUrl(CFG.spendUrl())
      .addConverterFactory(JacksonConverterFactory.create())
      .build();

  private final SpendApi spendApi = retrofit.create(SpendApi.class);

  @Override
  public SpendJson createSpend(SpendJson spend) {
      final Response<SpendJson> response;
      try {
            response = spendApi.createSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
      }
    assertEquals(200, response.code());
    return response.body();
  }

  @Override
  public SpendJson updateSpend(SpendJson spend) {
      final Response<SpendJson> response;
      try {
          response = spendApi.updateSpend(spend)
                  .execute();
      } catch (IOException e) {
          throw new AssertionError(e);
      }
      assertEquals(200, response.code());
      return response.body();
  }

    @Override
    public SpendJson getSpendById(String id) {
        return null;
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String username) {
        return List.of();
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String username, String filterCurrency) {
        return List.of();
    }

    @Override
    public void deleteSpendById(String id) {

    }

    @Override
    public void deleteSpendByIds(List<String> ids) {

    }
}
