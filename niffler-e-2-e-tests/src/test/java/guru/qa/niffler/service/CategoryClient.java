package guru.qa.niffler.service;

import guru.qa.niffler.model.CategoryJson;
import java.util.List;

public interface CategoryClient {

    CategoryJson createCategory(CategoryJson category);

    CategoryJson updateCategory(CategoryJson category);

    List<CategoryJson> getAllCategoriesByUsername(String username, boolean excludeArchived);
}
