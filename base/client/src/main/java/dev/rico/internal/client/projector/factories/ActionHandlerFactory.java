package dev.rico.internal.client.projector.factories;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import dev.rico.client.projector.Projector;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.ToggleItemModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public interface ActionHandlerFactory {

    default EventHandler<ActionEvent> createOnActionHandler(final String type, final IdentifiableModel identifiableModel, final Projector projector) {
        Assert.requireNonNull(projector, "projector");
        return event -> {
            final ControllerProxy<? extends ManagedUiModel> controllerProxy = projector.getControllerProxy();
            Assert.requireNonNull(controllerProxy, "controllerProxy");

            final Map<IdentifiableModel, Node> modelToNodeMap = projector.getModelToNodeMap();
            Assert.requireNonNull(modelToNodeMap, "modelToNodeMap");

            event.consume();

            if (identifiableModel instanceof ButtonModel && ((ButtonModel) identifiableModel).getAction() != null) {
                final String action = ((ButtonModel) identifiableModel).getAction();

                final CompletableFuture<Void> actionInvocation = controllerProxy.invoke(action);
                Assert.requireNonNull(actionInvocation, "actionInvocation");
                actionInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));

                final CompletableFuture<Void> typeInvocation = controllerProxy.invoke(type, new Param("button", identifiableModel));
                Assert.requireNonNull(typeInvocation, "typeInvocation");
                typeInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));

            } else if (identifiableModel instanceof ToggleItemModel && ((ToggleItemModel) identifiableModel).getAction() != null) {
                final String action = ((ToggleItemModel) identifiableModel).getAction();

                final CompletableFuture<Void> actionInvocation = controllerProxy.invoke(action);
                Assert.requireNonNull(actionInvocation, "actionInvocation");
                actionInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));

                final CompletableFuture<Void> typeInvocation = controllerProxy.invoke(type, new Param("button", identifiableModel));
                Assert.requireNonNull(typeInvocation, "typeInvocation");
                typeInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));

            } else {
                final CompletableFuture<Void> typeInvocation = controllerProxy.invoke(type, new Param("button", identifiableModel));
                Assert.requireNonNull(typeInvocation, "typeInvocation");
                typeInvocation.exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            }
        };
    }
}
