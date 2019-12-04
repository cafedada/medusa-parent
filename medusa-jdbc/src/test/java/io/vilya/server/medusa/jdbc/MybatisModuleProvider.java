package io.vilya.server.medusa.jdbc;

import com.google.inject.Module;

/**
 * @author cafedada
 */
public class MybatisModuleProvider implements ModuleProvider {

    @Override
    public Module get() {
        return new MyBatisModule();
    }

}
