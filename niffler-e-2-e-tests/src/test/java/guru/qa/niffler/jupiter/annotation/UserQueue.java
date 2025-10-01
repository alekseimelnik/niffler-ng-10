package guru.qa.niffler.jupiter.annotation;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;

@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@ExtendWith(UsersQueueExtension.class)
public @interface UserQueue {

}
