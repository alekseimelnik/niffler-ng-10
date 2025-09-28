package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;

public interface SpendClient {

    SpendJson createSpend(SpendJson spend);

    SpendJson updateSpend(SpendJson spend);

    SpendJson getSpendById(String id);

    List<SpendJson> getAllSpendsByUsername(String username);

    List<SpendJson> getAllSpendsByUsername(String username, String filterCurrency);

    void deleteSpendById(String id);

    void deleteSpendByIds(String username, List<String> ids);
}
