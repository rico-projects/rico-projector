package dev.rico.internal.client.projector;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.internal.projector.ui.dialog.DialogModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import org.jpedal.parser.shape.N;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

public class JavaFXProjectorImpl implements Projector {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

    private final Map<Class<? extends ItemModel>, ProjectorNodeFactory> factories;

    private final Map<Class<? extends DialogModel>, ProjectorDialogHandler> dialogHandlers;

    //TODO: REFACTOR
    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();

    private final SimpleObjectProperty<Node> root = new SimpleObjectProperty<>();

    public JavaFXProjectorImpl(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this.controllerProxy = controllerProxy;

        factories = new HashMap<>();
        try {
            ServiceLoader<ProjectorNodeFactory> serviceLoader = ServiceLoader.load(ProjectorNodeFactory.class);
            for (ProjectorNodeFactory factory : serviceLoader) {
                final Class<? extends ItemModel> type = factory.getSupportedType();
                if (type == null) {
                    throw new IllegalStateException("ProjectorNodeFactory implementation '" + factory.getClass() + "' method getSupportedType() must not return 'null'");
                }
                if (this.factories.containsKey(type)) {
                    throw new IllegalStateException("ProjectorNodeFactory implementation '" + factory.getClass() + "' for '" + type + "' is already defined by factory");
                }
                this.factories.put(type, factory);
            }
        } catch (final Exception e) {
            throw new RuntimeException("Error in loading ui component factories", e);
        }

        dialogHandlers = new HashMap<>();
        try {
            ServiceLoader<ProjectorDialogHandler> serviceLoader = ServiceLoader.load(ProjectorDialogHandler.class);
            for (ProjectorDialogHandler handler : serviceLoader) {
                final Class<? extends DialogModel> type = handler.getSupportedType();
                if (type == null) {
                    throw new IllegalStateException("ProjectorDialogHandler implementation '" + handler.getClass() + "' method getSupportedType() must not return 'null'");
                }
                if (this.dialogHandlers.containsKey(type)) {
                    throw new IllegalStateException("ProjectorDialogHandler implementation '" + handler.getClass() + "' for '" + type + "' is already defined by handler");
                }
                this.dialogHandlers.put(type, handler);
            }
        } catch (final Exception e) {
            throw new RuntimeException("Error in loading ui component factories", e);
        }

        ManagedUiModel model = controllerProxy.getModel();
        model.rootProperty().onChanged(evt -> updateUiRoot(evt.getNewValue()));
        updateUiRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
            DialogModel newDialog = event.getNewValue();
            if(newDialog != null) {
                final ProjectorDialogHandler projectorDialogHandler = dialogHandlers.get(newDialog.getClass());
                projectorDialogHandler.show(this, newDialog);
            }
        }));
    }

    private void updateUiRoot(ItemModel itemModel) {
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
        return (N) factory.create(this, itemModel);
    }

    @Override
    public ControllerProxy<? extends ManagedUiModel> getControllerProxy() {
        return controllerProxy;
    }

    public WeakHashMap<IdentifiableModel, Node> getModelToNodeMap() {
        return modelToNodeMap;
    }

    @Override
    public PostProcessor getPostProcessor() {
        throw new RuntimeException("Not implemented.");
    }
}
