package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.service.CategoryApiClient;
import guru.qa.niffler.service.CategoryClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.DataUtils.getRandomCategoryName;

public class CreateCategoryExtension implements
        BeforeEachCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);
    private final CategoryClient categoryClient = new CategoryApiClient();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                Category.class
        ).ifPresent(annotation -> {
            CategoryJson created = categoryClient.createCategory(
                    new CategoryJson(
                            null,
                            getRandomCategoryName(),
                            annotation.username(),
                            annotation.archived()
                    )
            );
            if (annotation.archived()) {
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
        });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) throws Exception {
        try {
            CategoryJson category = context.getStore(NAMESPACE)
                    .get(context.getUniqueId(), CategoryJson.class);
            if (!category.archived()) {
                CategoryJson archivedCategory = new CategoryJson(
                        category.id(),
                        category.name(),
                        category.username(),
                        true
                );
                categoryClient.updateCategory(archivedCategory);
            }
        } catch (Exception e) {
            System.err.println("Exception during category cleanup: " + e.getMessage());
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
