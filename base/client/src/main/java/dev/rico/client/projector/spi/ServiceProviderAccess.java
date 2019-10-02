package dev.rico.client.projector.spi;

import dev.rico.internal.core.Assert;

import java.util.*;

public interface ServiceProviderAccess {
    default <T, S extends TypeBasedProvider<T>> Map<Class<? extends T>, List<S>> loadServiceProviders(final Class<S> serviceClass) {
        Assert.requireNonNull(serviceClass, "serviceClass");
        final Map<Class<? extends T>, List<S>> map = new HashMap<>();
        final ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceClass);
        for (final S provider : serviceLoader) {
            final Class<? extends T> type = provider.getSupportedType();
            if (type == null) {
                throw new IllegalStateException("Supported type of " + serviceClass.getSimpleName() + " implementation '" + provider.getClass() + "' must not be 'null'");
            }
            List<S> list = map.computeIfAbsent(type, aClass -> new LinkedList<>());
            Optional<S> withSamePriority = list.stream().filter(p -> p.getPriority() == provider.getPriority()).findAny();
            if (withSamePriority.isPresent()) {
                throw new IllegalArgumentException("There should be only one provider with priority '" + provider.getPriority() + "' for type '" +
                        provider.getSupportedType().getSimpleName() + "' but found: " + withSamePriority.get().getClass() + " and " + provider.getClass());
            }
            list.add(provider);
        }
        map.forEach((aClass, list) -> list.sort(Comparator.comparing(TypeBasedProvider::getPriority, Comparator.reverseOrder())));
        return map;
    }
}
