package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.IdentifiableModel;

import java.util.function.Consumer;

public interface Retained<T extends IdentifiableModel> {
    T get();

    default boolean isPresent() {
        return get() != null;
    }

    default void ifPresent(Consumer<T> consumer) {
        consumer.accept(get());
    }

    default void notPresent(Runnable consumer) {
        if (get() == null) {
            consumer.run();
        }
    }

    static <T extends IdentifiableModel> Retained<T> empty() {
        return new Keeper<>();
    }
}
