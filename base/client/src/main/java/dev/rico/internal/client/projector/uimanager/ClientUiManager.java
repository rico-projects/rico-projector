package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.JavaFXProjectorImpl;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import to.remove.DolphinEventHandler;

import java.util.function.Function;

public class ClientUiManager {

    private JavaFXProjectorImpl projector;

    public ClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this(controllerProxy, null, null, null);
    }

    public ClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy, DolphinEventHandler handler, PostProcessor postProcessor, Function<String, Node> customComponentSupplier) {
        projector = new JavaFXProjectorImpl(controllerProxy, postProcessor);
    }

    public Node createNode(ItemModel itemModel) {
        return projector.createNode(itemModel);
    }

    public SimpleObjectProperty<Node> rootProperty() {
        return projector.rootProperty();
    }
}
