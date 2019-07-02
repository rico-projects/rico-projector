package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface ActionHandlerFactory {

    default EventHandler<ActionEvent> createOnActionHandler(final String type, final IdentifiableModel identifiableModel, final Projector projector) {
        Assert.requireNonNull(projector, "projector");
        final Map<IdentifiableModel, Node> modelToNodeMap = projector.getModelToNodeMap();
        Assert.requireNonNull(modelToNodeMap, "modelToNodeMap");
        return createOnActionHandler(type, modelToNodeMap.get(identifiableModel), projector);
    }

    default EventHandler<ActionEvent> createOnActionHandler(final String type, final Node node, final Projector projector) {
        Assert.requireNonNull(type, "type");
        Assert.requireNonNull(node, "node");
        Assert.requireNonNull(projector, "projector");
        final ControllerProxy<? extends ManagedUiModel> controllerProxy = projector.getControllerProxy();
        Assert.requireNonNull(controllerProxy, "controllerProxy");
        return event -> {
            event.consume();
            final CompletableFuture<Void> typeInvocation = controllerProxy.invoke(type);
            Assert.requireNonNull(typeInvocation, "typeInvocation");
            typeInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(node, throwable));
        };
    }
}
