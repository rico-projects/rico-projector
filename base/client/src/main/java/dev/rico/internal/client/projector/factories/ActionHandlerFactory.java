package dev.rico.internal.client.projector.factories;

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

import java.util.WeakHashMap;

public interface ActionHandlerFactory {

    default EventHandler<ActionEvent> createOnActionHandler(final String type, final IdentifiableModel identifiableModel, final Projector projector) {
        Assert.requireNonNull(projector, "projector");
        return event -> {
            final ControllerProxy<? extends ManagedUiModel> controllerProxy = projector.getControllerProxy();
            Assert.requireNonNull(controllerProxy, "controllerProxy");

            final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = projector.getModelToNodeMap();
            Assert.requireNonNull(modelToNodeMap, "modelToNodeMap");

            event.consume();
            if (identifiableModel instanceof ButtonModel && ((ButtonModel) identifiableModel).getAction() != null) {
                String action = ((ButtonModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else if (identifiableModel instanceof ToggleItemModel && ((ToggleItemModel) identifiableModel).getAction() != null) {
                String action = ((ToggleItemModel) identifiableModel).getAction();
                controllerProxy.invoke(action).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            } else {
                controllerProxy.invoke(type, new Param("button", identifiableModel)).exceptionally(throwable -> UnexpectedErrorDialog.showError(modelToNodeMap.get(identifiableModel), throwable));
            }
        };
    }
}
