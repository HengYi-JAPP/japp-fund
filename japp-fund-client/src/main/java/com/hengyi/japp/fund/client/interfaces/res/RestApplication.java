package com.hengyi.japp.fund.client.interfaces.res;

import com.google.common.reflect.ClassPath;

import javax.ws.rs.Path;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class RestApplication extends Application {
    private final String packageName = this.getClass().getPackage().getName();
    private final Set<Object> singletons;

    public RestApplication() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        try {
            singletons = ClassPath.from(loader).getTopLevelClasses()
                    .stream()
                    .parallel()
                    .filter(info -> info.getName().startsWith(packageName))
                    .map(ClassPath.ClassInfo::load)
                    .filter(clazz -> {
                        final Path path = clazz.getAnnotation(Path.class);
                        if (path != null) {
                            return true;
                        }
                        final Provider provider = clazz.getAnnotation(Provider.class);
                        if (provider != null) {
                            return true;
                        }
                        return false;
                    })
                    .distinct()
                    .map(clazz -> {
                        try {
                            return clazz.newInstance();
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }
}
