package guru.qa.niffler.jupiter.annotation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RUNTIME)
@Target({TYPE, METHOD})
@ExtendWith(UsersQueueExtension.class)
public @interface UserQueue {

}
