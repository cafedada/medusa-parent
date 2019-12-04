package io.vilya.server.medusa.jdbc;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.junit.platform.commons.util.ReflectionUtils;
import com.google.common.collect.Streams;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * @author cafedada
 */
public class GuiceExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context)
            throws Exception {
        Class<?> testInstanceType = testInstance.getClass();

        EnableGuiceModule annotation = testInstanceType.getAnnotation(EnableGuiceModule.class);
        if (annotation == null) {
            return;
        }

        Class<? extends Module>[] moduleTypes = annotation.modules();
        Class<? extends ModuleProvider>[] moduleProviderTypes = annotation.moduleProviders();

        List<Module> modules = Streams
                .concat(Arrays.stream(moduleTypes).map(ReflectionUtils::newInstance),
                        Arrays.stream(moduleProviderTypes).map(ReflectionUtils::newInstance)
                                .map(Supplier::get))
                .collect(Collectors.toList());
        
        Injector injector = Guice.createInjector(modules);
        injector.injectMembers(testInstance);
    }

}
