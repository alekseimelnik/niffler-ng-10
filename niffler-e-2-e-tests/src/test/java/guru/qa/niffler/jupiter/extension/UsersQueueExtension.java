package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.model.StaticUser;
import io.qameta.allure.Allure;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.*;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Arrays;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class UsersQueueExtension implements
    BeforeTestExecutionCallback,
    AfterTestExecutionCallback,
    ParameterResolver {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      UsersQueueExtension.class);

  private static final Map<UserType.FriendType, Queue<StaticUser>> USERS = new ConcurrentHashMap<>();

  static {
    USERS.put(UserType.FriendType.EMPTY,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("bee", "12345", null, null, null))
        ));
    USERS.put(UserType.FriendType.WITH_FRIEND,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("duck", "12345", "alex", null, null))
        ));
    USERS.put(UserType.FriendType.WITH_INCOME_REQUEST,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("dima", "12345", null, "barsik", null))
        ));
    USERS.put(UserType.FriendType.WITH_OUTCOME_REQUEST,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("barsik", "12345", null, null, "dima"))
        ));

  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    List<UserType.FriendType> types = Arrays.stream(context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .map(p -> p.getAnnotation(UserType.class).value())
        .distinct()
        .toList();

    if (types.isEmpty()) {
      return;
    }

    Map<UserType.FriendType, StaticUser> users = (Map<UserType.FriendType, StaticUser>)
        context.getStore(NAMESPACE).getOrComputeIfAbsent(
            context.getUniqueId(),
            key -> new HashMap<>()
        );

    for (UserType.FriendType type : types) {
      if (users.containsKey(type)) {
        continue;
      }

      Queue<StaticUser> queue = USERS.get(type);
      if (queue == null) {
        throw new IllegalStateException("No users queue configured for type: " + type);
      }

      Optional<StaticUser> testUser = Optional.empty();
      StopWatch sw = StopWatch.createStarted();

      while (testUser.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
        testUser = Optional.ofNullable(queue.poll());
      }

      Allure.getLifecycle().updateTestCase(testCase ->
          testCase.setStart(new Date().getTime())
      );

      testUser.ifPresentOrElse(
          u -> users.put(type, u),
          () -> {
            throw new IllegalStateException(
                "Can't obtain user for type " + type + " after 30s"
            );
          }
      );
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<UserType.FriendType, StaticUser> map = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );
    if (map == null || map.isEmpty()) {
      return;
    }
    for (
        Map.Entry<UserType.FriendType, StaticUser> e : map.entrySet()
    ) {
      Queue<StaticUser> queue = USERS.get(e.getKey());
      if (queue != null) {
        queue.add(e.getValue());
      }
    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
        && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public StaticUser resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return (StaticUser) extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), Map.class)
        .get(parameterContext.getParameter()
            .getAnnotation(UserType.class)
            .value()
        );
  }
}
