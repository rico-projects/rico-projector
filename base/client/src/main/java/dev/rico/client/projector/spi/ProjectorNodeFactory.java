package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import javafx.scene.Node;

import java.util.function.Supplier;

public interface ProjectorNodeFactory<M extends IdentifiableModel, N extends Node> extends TypeBasedProvider<M> {

    N create(Projector projector, M model);

    default <S> S getValue(S value, S fallbackValue) {
        Assert.requireNonNull(fallbackValue, "fallbackValue");
        if (value == null) {
            return fallbackValue;
        } else {
            return value;
        }
    }

    default <S> S getValue(final S fromBinding, final Supplier<S> fallbackGetter) {
        Assert.requireNonNull(fallbackGetter, "fallbackGetter");
        return fromBinding == null ? fallbackGetter.get() : fromBinding;
    }
}
