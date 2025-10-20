package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryClient;
import guru.qa.niffler.service.CategoryDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.DataUtils.getRandomCategoryName;

public class CreateCategoryExtension implements
    BeforeEachCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      CreateCategoryExtension.class);
  private final CategoryClient categoryClient = new CategoryDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(userAnnotation -> {
      if (userAnnotation.categories().length > 0) {
        Category categoryAnnotation = userAnnotation.categories()[0];
        CategoryJson created = categoryClient.createCategory(
            new CategoryJson(
                null,
                getRandomCategoryName(),
                userAnnotation.username(),
                false
            )
        );
        if (categoryAnnotation.archived()) {
          CategoryJson archivedCategory = new CategoryJson(
              created.id(),
              created.name(),
              created.username(),
              true
          );
          created = categoryClient.updateCategory(archivedCategory);
        }
        context.getStore(NAMESPACE).put(
            context.getUniqueId(),
            created
        );
      }
    });
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    CategoryJson category = context.getStore(NAMESPACE)
        .get(context.getUniqueId(), CategoryJson.class);
    if (category == null) {
      return;
    }
    if (!category.archived()) {
      CategoryJson archivedCategory = new CategoryJson(
          category.id(),
          category.name(),
          category.username(),
          true
      );
      categoryClient.updateCategory(archivedCategory);
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
  }

  @Override
  public Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), CategoryJson.class);
  }
}
