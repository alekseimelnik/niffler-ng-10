package guru.qa.niffler.service.impl;

import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;

public interface SpendClient {

  SpendJson createSpend(SpendJson spend);

  CategoryJson createCategory(CategoryJson category);

  CategoryJson updateCategory(CategoryJson category);
}
