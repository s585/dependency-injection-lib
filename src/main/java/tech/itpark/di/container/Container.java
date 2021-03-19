package tech.itpark.di.container;

import java.util.List;

public interface Container {
    void register(List<Class<?>> classes);

    void wire();
}
