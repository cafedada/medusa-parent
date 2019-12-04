package io.vilya.server.medusa.jdbc;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import com.google.inject.Module;


/**
 * @author cafedada
 */
@Documented
@Retention(RUNTIME)
@Target(TYPE)
public @interface EnableGuiceModule {

    Class<? extends Module>[] modules() default {};
    
    Class<? extends ModuleProvider>[] moduleProviders() default {};
    
}
