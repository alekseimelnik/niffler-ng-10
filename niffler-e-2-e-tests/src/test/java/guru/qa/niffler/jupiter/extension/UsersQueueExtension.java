package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.UserType;
import guru.qa.niffler.model.StaticUser;
import java.lang.reflect.Parameter;
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
            List.of(new StaticUser("duck", "12345", "dima", null, null))
        ));
    USERS.put(UserType.FriendType.WITH_INCOME_REQUEST,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("dima", "12345", null, "bee", null))
        ));
    USERS.put(UserType.FriendType.WITH_OUTCOME_REQUEST,
        new ConcurrentLinkedQueue<>(
            List.of(new StaticUser("barsik", "12345", null, null, "bill"))
        ));

  }

  @Override
  public void beforeTestExecution(ExtensionContext context) {
    List<Parameter> testMethods = Arrays.stream(
            context.getRequiredTestMethod().getParameters())
        .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
        .toList();

    Map<UserType.FriendType, StaticUser> users = new HashMap<>();

    for (Parameter parameter : testMethods) {
      UserType.FriendType friendType = parameter.getAnnotation(UserType.class).value();
      if (users.containsKey(friendType)) {
        continue;
      }

      Optional<StaticUser> testUser = Optional.empty();

      Queue<StaticUser> queue = USERS.get(friendType);
      if (queue == null) {
        throw new IllegalStateException("No users for type: " + friendType);
      }

      StopWatch sw = StopWatch.createStarted();
      while (testUser.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
        testUser = Optional.ofNullable(queue.poll());
      }
      StaticUser staticUser = testUser.orElseThrow(
          () -> new IllegalStateException("Can't obtain user for type " + friendType + " after 30s")
      );
      users.put(friendType, staticUser);
    }

    if (!users.isEmpty()) {
      context.getStore(NAMESPACE).put(context.getUniqueId(), users);
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<UserType.FriendType, StaticUser> users = context.getStore(NAMESPACE).get(
        context.getUniqueId(),
        Map.class
    );
    if (users == null || users.isEmpty()) {
      return;
    }
    for (
        Map.Entry<UserType.FriendType, StaticUser> user : users.entrySet()
    ) {
      Queue<StaticUser> queue = USERS.get(user.getKey());
      if (queue != null) {
        queue.add(user.getValue());
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
