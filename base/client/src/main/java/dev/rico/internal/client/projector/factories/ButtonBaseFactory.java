package dev.rico.internal.client.projector.factories;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.FXBinder;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.mixed.CommonUiHelper;
import dev.rico.internal.projector.ui.ButtonModel;
import dev.rico.remoting.Property;
import javafx.beans.property.ObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import static dev.rico.client.remoting.FXBinder.bind;
import static java.util.Objects.requireNonNull;

public abstract class ButtonBaseFactory<T extends ButtonModel, S extends ButtonBase> implements ProjectorNodeFactory<T, S>, ActionHandlerFactory {

    protected S createButtonBase(final Projector projector, final T model, final S node) {
        configureButton(model, node);
        installMonitoredAction(node, createOnActionHandler("buttonClick", model, projector));
        return node;
    }

    protected void configureButton(final T model, final S node) {
        Assert.requireNonNull(model, "model");
        Assert.requireNonNull(node, "node");

        bind(node.textProperty()).to(model.captionProperty());
        subscribe(model.tooltipProperty(), tooltipOptional -> createTooltip(tooltipOptional, node));
        subscribe(model.imageProperty(), optionalImagePath -> createImage(optionalImagePath, model, node));
    }

    private static <T> void subscribe(Property<T> property, Consumer<T> consumer) {
        CommonUiHelper.subscribe(property, evt -> consumer.accept(evt.getNewValue()));
    }


    private void installMonitoredAction(final S node, final EventHandler<ActionEvent> onActionHandler) {
        Assert.requireNonNull(node, "node");

        final List<EventHandler<ActionEvent>> handlers = new LinkedList<>();
        final EventHandler<ActionEvent> actionEventEventHandler = event -> handlers.forEach(handler -> handler.handle(event));
        handlers.add(onActionHandler);
        if (node.getOnAction() != null) {
            handlers.add(node.getOnAction());
        }
        node.setOnAction(actionEventEventHandler);
        final ObjectProperty<EventHandler<ActionEvent>> actionHandlerProperty = node.onActionProperty();
        Assert.requireNonNull(actionHandlerProperty, "actionHandlerProperty");
        actionHandlerProperty.addListener((observable, oldValue, newValue) -> {
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

    private void createTooltip(final String tooltipText, final S node) {
        if (tooltipText != null) {
            final Tooltip tooltip = new Tooltip(tooltipText);
            node.setTooltip(tooltip);
        } else {
            node.setTooltip(null);
        }

    }

    private void createImage(final String imagePath, final T model, final S node) {
        if (imagePath != null) {
            final String newImagePath = "/image/" + imagePath;
            final URL resource = ButtonBaseFactory.class.getResource(newImagePath);
            requireNonNull(resource, "Could not find classpath resource '" + newImagePath + "'");
            final ImageView graphic = new ImageView(new javafx.scene.image.Image(resource.toExternalForm()));
            graphic.setPreserveRatio(true);
            FXBinder.bind(graphic.fitHeightProperty()).to(model.prefHeightProperty());
            node.setGraphic(graphic);
        } else {
            node.setGraphic(null);
        }
    }
}
