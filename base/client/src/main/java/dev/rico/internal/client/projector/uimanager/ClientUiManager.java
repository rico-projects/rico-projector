package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.JavaFXProjectorImpl;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class ClientUiManager {

    private final JavaFXProjectorImpl projector;

    public ClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this(controllerProxy, null);
    }

    public ClientUiManager(final ControllerProxy<? extends ManagedUiModel> controllerProxy, final PostProcessor postProcessor) {
        projector = new JavaFXProjectorImpl(controllerProxy, postProcessor);
    }

    public Node createNode(final ItemModel itemModel) {
        return projector.createNode(itemModel);
    }

    public SimpleObjectProperty<Node> rootProperty() {
        return projector.rootProperty();
    }
}
