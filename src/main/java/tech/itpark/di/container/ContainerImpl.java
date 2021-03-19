package tech.itpark.di.container;

import tech.itpark.di.exception.AmbiguousConstructorException;
import tech.itpark.di.exception.DIException;
import tech.itpark.di.exception.UnmetDependencyException;

import java.lang.reflect.Constructor;
import java.util.*;

public class ContainerImpl implements Container {

    private final Map<Class<?>, Object> objects = new HashMap<>();
    private final List<Class<?>> definitions = new LinkedList<>();

    @Override
    public void register(List<Class<?>> classes) {
        this.definitions.addAll(classes);
    }

    @Override
    public void wire() {
        int instanced = 0;
        while (true) {
            int count = instanced;
            final Iterator<Class<?>> iterator = definitions.iterator();
            if (definitions.size() == 0) {
                break;
            }
            while (iterator.hasNext()) {
                final Class<?> clazz = iterator.next();
                final Constructor<?>[] constructors = clazz.getConstructors();
                if (constructors.length != 1) {
                    throw new AmbiguousConstructorException("component must have only one public constructor");
                }
                final Constructor<?> constructor = constructors[0];
                final Class<?>[] parameterTypes = constructor.getParameterTypes();

                if (parameterTypes.length == 0) {
                    try {
                        final Object o = constructor.newInstance();
                        objects.put(o.getClass(), o);
                    } catch (Exception e) {
                        throw new DIException(e);
                    }
                    iterator.remove();
                    instanced++;
                    continue;
                }

                final List<Object> parameters = new LinkedList<>();
                for (Class<?> parameterType : parameterTypes) {
                    final Object parameter = objects.get(Class.class);
                    if (parameter == null) {
                        throw new UnmetDependencyException(
                                "unmet dependency for "
                                        + clazz.getName()
                                        + " not found parameter with type "
                                        + parameterType.getName()
                        );
                    }
                    parameters.add(parameter);
                }

                if (objects.keySet().containsAll(Arrays.asList(parameterTypes))) {
                    try {
                        final Object o = constructor.newInstance(parameters.toArray());
                        objects.put(o.getClass(), o);
                    } catch (Exception e) {
                        throw new DIException(e);
                    }
                    iterator.remove();
                    instanced++;
                }

                if (count == instanced) {
                    throw new UnmetDependencyException("Unmet dependency for " + definitions.toString());
                }
            }
        }
    }
}
