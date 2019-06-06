package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import javafx.scene.Node;

public interface ProjectorNodeFactory<M extends IdentifiableModel, N extends Node> {

    N create(Projector projector, M model);

    default <S> S getValue(S value, S fallbackValue) {
        Assert.requireNonNull(fallbackValue, "fallbackValue");
        if (value == null) {
            return fallbackValue;
        } else {
            return value;
        }
    }

    Class<M> getSupportedType();
}