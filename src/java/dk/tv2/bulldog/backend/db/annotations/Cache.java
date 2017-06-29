/*
 */
package dk.tv2.bulldog.backend.db.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author migo
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Cache {

    /**
     *
     * @return an instance of Managers
     */
    public Class<?> manager();
}
