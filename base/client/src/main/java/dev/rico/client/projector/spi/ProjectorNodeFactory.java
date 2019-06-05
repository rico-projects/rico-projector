package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.IdentifiableModel;
import javafx.scene.Node;

public interface ProjectorNodeFactory<M extends IdentifiableModel, N extends Node> {

    N create(Projector projector, M model);

    Class<M> getSupportedType();
}
