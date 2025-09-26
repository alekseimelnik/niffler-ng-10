package guru.qa.niffler.api;

import guru.qa.niffler.model.CategoryJson;
import retrofit2.Call;
import retrofit2.http.*;

public interface CategoryApi {

  @POST("internal/categories/add")
    Call<CategoryJson> createCategory(@Query("category") CategoryJson category);

  @PATCH("internal/categories/update")
    Call<CategoryJson> updateCategory(@Query("category") CategoryJson category);

  @GET("internal/categories/all")
    Call<CategoryJson[]> getAllCategoriesByUsername(@Query("username") String username,
                                                    @Query("excludeArchived") boolean excludeArchived);
}