package dev.rico.internal.server.projector;

import java.util.function.Consumer;

import dev.rico.internal.projector.ui.IdentifiableModel;

public interface Retained<T extends IdentifiableModel> {

    T get();

    default boolean isPresent() {
        return get() != null;
    }

    default void ifPresent(final Consumer<T> consumer) {
        consumer.accept(get());
    }

    default void notPresent(final Runnable consumer) {
        if (get() == null) {
            consumer.run();
        }
    }

    static <T extends IdentifiableModel> Retained<T> empty() {
        return new Keeper<>();
    }

}
