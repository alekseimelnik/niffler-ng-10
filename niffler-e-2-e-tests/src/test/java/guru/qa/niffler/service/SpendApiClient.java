package guru.qa.niffler.service;


import guru.qa.niffler.api.SpendApi;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.SpendJson;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.apache.hc.core5.http.HttpStatus.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpendApiClient implements SpendClient {

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
        assertEquals(SC_CREATED, response.code());
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
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public SpendJson getSpendById(String id) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpendById(id)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return response.body();
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String username) {
        final Response<SpendJson[]> response;
        try {
            response = spendApi.getAllSpendsByUsername(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
    }

    @Override
    public List<SpendJson> getAllSpendsByUsername(String username, String filterCurrency) {
        final Response<SpendJson[]> response;
        try {
            response = spendApi.getAllSpendsByUsername(username, filterCurrency)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_OK, response.code());
        return List.of(Objects.requireNonNullElseGet(response.body(), () -> new SpendJson[0]));
    }

    @Override
    public void deleteSpendById(String id) {
        final Response<Void> response;
        try {
            response = spendApi.deleteSpendById(id)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }

    @Override
    public void deleteSpendByIds(String username, List<String> ids) {
        final Response<Void> response;
        try {
            response = spendApi.deleteSpendByIds(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(SC_ACCEPTED, response.code());
    }
}
