package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.WebTest;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Test;

@WebTest
public class CategoryArchiveTest {

    @Category(
            username = "aleksei",
            archived = false
    )
    @Test
    void archivedCategoryShouldPresentInCategoryList(CategoryJson category){

    }

    @Category(
            username = "aleksei",
            archived = true
    )
    @Test
    void activeCategoryShouldPresentInCategoryList(CategoryJson category) {
    }
}
