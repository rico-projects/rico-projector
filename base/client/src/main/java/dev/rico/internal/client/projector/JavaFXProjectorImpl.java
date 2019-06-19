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

import java.util.*;

public class JavaFXProjectorImpl implements Projector {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

    private final PostProcessor postProcessor;

    private final Map<Class<? extends ItemModel>, ProjectorNodeFactory> factories;

    private final Map<Class<? extends DialogModel>, ProjectorDialogHandler> dialogHandlers;

    //TODO: REFACTOR
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
        final ProjectorNodeFactory factory = factories.get(itemModel.getClass());
        if (factory == null) {
            throw new IllegalArgumentException("No factory found for " + itemModel.getClass());
        }
        N node = (N) factory.create(this, itemModel);
        postProcess(node, itemModel);
        return node;
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
