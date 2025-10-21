package guru.qa.niffler.jupiter.extension;

import static guru.qa.niffler.utils.DataUtils.getRandomCategoryName;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.spend.CategoryJson;
import guru.qa.niffler.model.spend.SpendJson;
import guru.qa.niffler.service.impl.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      SpendingExtension.class);

  private final SpendClient spendClient = new SpendDbClient();

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation -> {
          if (userAnnotation.spendings().length > 0) {
            Spending spendingAnnotation = userAnnotation.spendings()[0];
            SpendJson created = spendClient.createSpend(
                new SpendJson(
                    null,
                    new Date(),
                    new CategoryJson(
                        null,
                        getRandomCategoryName(),
                        userAnnotation.username(),
                        false
                    ),
                    spendingAnnotation.currency(),
                    spendingAnnotation.amount(),
                    spendingAnnotation.description(),
                    userAnnotation.username()
                )
            );
            context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                created
            );
          }
        }
    );
  }

    @Override
    public boolean supportsParameter (ParameterContext parameterContext, ExtensionContext
    extensionContext) throws ParameterResolutionException {
      return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter (ParameterContext parameterContext, ExtensionContext
    extensionContext) throws ParameterResolutionException {
      return extensionContext.getStore(NAMESPACE)
          .get(extensionContext.getUniqueId(), SpendJson.class);
    }
  }
