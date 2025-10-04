package guru.qa.niffler.jupiter.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(PARAMETER)
@Retention(RUNTIME)
public @interface UserType {
  FriendType value() default FriendType.EMPTY;

  enum FriendType {
    EMPTY,
    WITH_FRIEND,
    WITH_INCOME_REQUEST,
    WITH_OUTCOME_REQUEST
  }
}
