package guru.qa.niffler.service.impl;

import guru.qa.niffler.model.spend.CategoryJson;
import java.util.List;

public interface CategoryClient {

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    List<CategoryJson> getAllCategoriesByUsername(String username, boolean excludeArchived);
}
