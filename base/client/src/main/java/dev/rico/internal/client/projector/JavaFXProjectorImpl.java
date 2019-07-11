package dev.rico.internal.client.projector;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.projector.spi.TypeBasedProvider;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.layout.Region;

import java.util.*;
import java.util.function.Supplier;

import static dev.rico.client.remoting.FXBinder.bind;

public class JavaFXProjectorImpl implements Projector {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

    private final PostProcessor postProcessor;

    private final Map<Class<? extends ItemModel>, ProjectorNodeFactory> factories;

    private final Map<Class<? extends DialogModel>, ProjectorDialogHandler> dialogHandlers;

    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();

    private final SimpleObjectProperty<Node> root = new SimpleObjectProperty<>();

    public JavaFXProjectorImpl(final ControllerProxy<? extends ManagedUiModel> controllerProxy, final PostProcessor postProcessor) {
        this.controllerProxy = Assert.requireNonNull(controllerProxy, "controllerProxy");
        this.postProcessor = Assert.requireNonNull(postProcessor, "postProcessor");

        try {
            factories = loadServiceProviders(ProjectorNodeFactory.class);
            dialogHandlers = loadServiceProviders(ProjectorDialogHandler.class);
        } catch (final Exception e) {
            throw new RuntimeException("Error in loading ui component factories", e);
        }

        final ManagedUiModel model = controllerProxy.getModel();
        Assert.requireNonNull(model, "model");
        model.rootProperty().onChanged(evt -> updateUiRoot(evt.getNewValue()));
        updateUiRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
            final DialogModel newDialog = event.getNewValue();
            if (newDialog != null) {
                final ProjectorDialogHandler projectorDialogHandler = dialogHandlers.get(newDialog.getClass());
                if (projectorDialogHandler == null) {
                    throw new IllegalStateException("No handler found for dialog type:" + newDialog.getClass());
                } else {
                    projectorDialogHandler.show(this, newDialog);
                }
            }
        }));
    }

    private <T, S extends TypeBasedProvider<T>> Map<Class<? extends T>, S> loadServiceProviders(final Class<S> serviceClass) {
        Assert.requireNonNull(serviceClass, "serviceClass");
        final Map<Class<? extends T>, S> map = new HashMap<>();
        final ServiceLoader<S> serviceLoader = ServiceLoader.load(serviceClass);
        for (final S provider : serviceLoader) {
            final Class<? extends T> type = provider.getSupportedType();
            if (type == null) {
                throw new IllegalStateException("Supported type of " + serviceClass.getSimpleName() + " implementation '" + provider.getClass() + "' must not be 'null'");
            }
            if (map.containsKey(type)) {
                final String className = Optional.ofNullable(map.get(type)).map(f -> f.getClass().getSimpleName()).orElse("UNKNOWN");
                throw new IllegalStateException("Provider for type '" + type + "' is already defined by factory. See concrete factories " + provider.getClass().getSimpleName() + " and " + className);
            }
            map.put(type, provider);
        }
        return map;
    }

    private void updateUiRoot(final ItemModel itemModel) {
        root.set(createNode(itemModel));
    }

    public Node getRoot() {
        return root.get();
    }

    public SimpleObjectProperty<Node> rootProperty() {
        return root;
    }

    @Override
    public <N extends Node> N createNode(final ItemModel itemModel) {
        if (itemModel == null) return null;
        final ProjectorNodeFactory factory = factories.get(itemModel.getClass());
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for " + itemModel.getClass());
        }
        N node = (N) factory.create(this, itemModel);
        bindDefaultProperties(node, itemModel);
        postProcess(node, itemModel);
        return node;
    }

    private <N extends Node> void bindDefaultProperties(final N node, final ItemModel<?> model) {
        if (node instanceof Region) {
            final Region region = (Region) node;
            bind(region.minWidthProperty()).to(model.minWidthProperty(), value -> fallback(value, region::getMinWidth));
            bind(region.minHeightProperty()).to(model.minHeightProperty(), value -> fallback(value, region::getMinHeight));
            bind(region.maxWidthProperty()).to(model.maxWidthProperty(), value -> fallback(value, region::getMaxWidth));
            bind(region.maxHeightProperty()).to(model.maxHeightProperty(), value -> fallback(value, region::getMaxHeight));
            bind(region.prefWidthProperty()).to(model.prefWidthProperty(), value -> fallback(value, region::getPrefWidth));
            bind(region.prefHeightProperty()).to(model.prefHeightProperty(), value -> fallback(value, region::getPrefHeight));
        }
        bind(node.disableProperty()).to(model.disableProperty(), value -> fallback(value, node::isDisable));
        bind(node.visibleProperty()).to(model.visibleProperty(), value -> fallback(value, node::isVisible));
        bind(node.managedProperty()).to(model.managedProperty(), value -> fallback(value, node::isManaged));
        model.getStyleClass().addAll(node.getStyleClass());
        bind(node.styleProperty()).to(model.styleProperty());
        bind(node.getStyleClass()).bidirectionalTo(model.getStyleClass());
    }

    private <T> T fallback(T fromBinding, Supplier<T> fallbackGetter) {
        return fromBinding == null ? fallbackGetter.get() : fromBinding;
    }

    private void postProcess(Node node, ItemModel itemModel) {
        itemModel.idProperty().onChanged(evt -> {
            String newId = evt.getNewValue();
            postProcessor.postProcess(newId, itemModel, node);
        });
        postProcessor.postProcess(itemModel.getId(), itemModel, node);
    }

    @Override
    public ControllerProxy<? extends ManagedUiModel> getControllerProxy() {
        return controllerProxy;
    }

    public Map<IdentifiableModel, Node> getModelToNodeMap() {
        return modelToNodeMap;
    }

    @Override
    public PostProcessor getPostProcessor() {
        return postProcessor;
    }
}
