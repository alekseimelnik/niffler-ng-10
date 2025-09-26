package guru.qa.niffler.api;

import guru.qa.niffler.model.SpendJson;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface SpendApi {
  @POST("internal/spends/add")
  Call<SpendJson> createSpend(@Body SpendJson spend);

  @PATCH("internal/spends/edit")
    Call<SpendJson> updateSpend(@Body SpendJson spend);

  @GET("internal/spends/{id}")
    Call<SpendJson> getSpendById(@Path("id") String id);

    @GET("internal/spends/all")
    Call<SpendJson[]> getAllSpendsByUsername(@Query("username") String username);

  @GET("internal/spends/all")
    Call<SpendJson[]> getAllSpendsByUsername(@Query("username") String username,
                                             @Query("filterCurrency") String filterCurrency);

  @DELETE("internal/spends/remove")
    Call<Void> deleteSpendById(@Query("username") String username);

  @DELETE("internal/spends/remove")
    Call<Void> deleteSpendById(@Query("ids") List<String> ids);
}