package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;

public interface CategoryClient {

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    List<CategoryJson> getAllCategoriesByUsername(String username, boolean excludeArchived);
}
