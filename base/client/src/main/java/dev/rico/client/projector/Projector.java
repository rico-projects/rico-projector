package dev.rico.client.projector;

import java.util.WeakHashMap;

import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.scene.Node;

public interface Projector {

    Node getRoot();

    <N extends Node> N createNode(ItemModel itemModel);

    ControllerProxy<? extends ManagedUiModel> getControllerProxy();

    @Deprecated
    WeakHashMap<IdentifiableModel, Node> getModelToNodeMap();

    @Deprecated
    PostProcessor getPostProcessor();
}
