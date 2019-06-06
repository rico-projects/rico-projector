package dev.rico.internal.client.projector.uimanager;

import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.JavaFXProjectorImpl;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.scene.Node;
import to.remove.DolphinEventHandler;

import java.util.function.Function;

public class NewClientUiManager {

    private JavaFXProjectorImpl projector;

    public NewClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this(controllerProxy, null, null, null);
    }

    public NewClientUiManager(ControllerProxy<? extends ManagedUiModel> controllerProxy, DolphinEventHandler handler, ClientUiManager.PostProcessor postProcessor, Function<String, Node> customComponentSupplier) {
        projector = new JavaFXProjectorImpl(controllerProxy);
    }

    public Node createNode(ItemModel itemModel) {
        return projector.createNode(itemModel);
    }
}
