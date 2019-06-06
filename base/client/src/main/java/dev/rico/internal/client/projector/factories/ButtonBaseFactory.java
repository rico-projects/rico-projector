package dev.rico.internal.client.projector.factories;

import static dev.rico.client.remoting.FXBinder.bind;
import static java.util.Objects.requireNonNull;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.WeakHashMap;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.client.remoting.Param;
import dev.rico.internal.client.projector.uimanager.ClientUiManager;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ToggleItemModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

public abstract class ButtonBaseFactory<T extends ButtonModel, S extends ButtonBase> implements ProjectorNodeFactory<T, S>, ActionHandlerFactory {

    S createButtonBase(final Projector projector, final T model, final S node) {
        configureButton(model, node);
        installMonitoredAction(node, createOnActionHandler("buttonClick", model, projector));
        return node;
    }

    protected void configureButton(final T model, final S node) {
        bind(node.textProperty()).to(model.captionProperty());
        CommonUiHelper.subscribeWithOptional(model.tooltipProperty(), tooltipOptional -> createTooltip(tooltipOptional, node));
        CommonUiHelper.subscribeWithOptional(model.imageProperty(), optionalImagePath -> createImage(optionalImagePath, model, node));
    }

    private void installMonitoredAction(final S node, final EventHandler<ActionEvent> onActionHandler) {
        final List<EventHandler<ActionEvent>> handlers = new LinkedList<>();
        final EventHandler<ActionEvent> actionEventEventHandler = event -> handlers.forEach(handler -> handler.handle(event));
        handlers.add(onActionHandler);
        if (node.getOnAction() != null) {
            handlers.add(node.getOnAction());
        }
        node.setOnAction(actionEventEventHandler);
        node.onActionProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == actionEventEventHandler) {
                return;
            }
            node.setOnAction(actionEventEventHandler);
            handlers.clear();
            handlers.add(onActionHandler);
            if (newValue != null) {
                handlers.add(newValue);
            }
        });
    }

    private void createTooltip(final Optional<String> tooltipOptional, final S node) {
        Tooltip tooltip = null;
        if (tooltipOptional.isPresent()) {
            tooltip = new Tooltip(tooltipOptional.get());
        }
        node.setTooltip(tooltip);
    }

    private void createImage(final Optional<String> optionalImagePath, final T model, final S node) {
        ImageView graphic = null;
        if (optionalImagePath.isPresent()) {
            final String imagePath = "/image/" + optionalImagePath.get();
            final URL resource = ClientUiManager.class.getResource(imagePath);
            requireNonNull(resource, "Could not find classpath resource '" + imagePath + "'");
            graphic = new ImageView(new javafx.scene.image.Image(resource.toExternalForm()));
            graphic.setPreserveRatio(true);
            FXBinder.bind(graphic.fitHeightProperty()).to(model.prefHeightProperty());
        }
        node.setGraphic(graphic);
    }
}
