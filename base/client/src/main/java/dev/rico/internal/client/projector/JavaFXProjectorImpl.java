package dev.rico.internal.client.projector;

import dev.rico.client.projector.PostProcessor;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

public class JavaFXProjectorImpl implements Projector {

    private final ControllerProxy<? extends ManagedUiModel> controllerProxy;

    private final Map<Class<? extends ItemModel>, ProjectorNodeFactory> factories;

    //TODO: REFACTOR
    private final WeakHashMap<IdentifiableModel, Node> modelToNodeMap = new WeakHashMap<>();

    private final SimpleObjectProperty<Node> root = new SimpleObjectProperty<>();

    public JavaFXProjectorImpl(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this.controllerProxy = controllerProxy;

        factories = new HashMap<>();
//        try {
            ServiceLoader<ProjectorNodeFactory> factories = ServiceLoader.load(ProjectorNodeFactory.class);
            for (ProjectorNodeFactory factory : factories) {
                final Class<? extends ItemModel> type = factory.getSupportedType();
                if (type == null) {
                    throw new IllegalStateException("ProjectorNodeFactory implementation '" + factory.getClass() + "' method getSupportedType() must not return 'null'");
                }
                if (this.factories.containsKey(type)) {
                    throw new IllegalStateException("ProjectorNodeFactory implementation '" + factory.getClass() + "' for '" + type + "' is already defined by factory");
                }
                this.factories.put(type, factory);
            }
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }

        ManagedUiModel model = controllerProxy.getModel();
        model.rootProperty().onChanged(evt -> updateUiRoot(evt.getNewValue()));
        updateUiRoot(model.getRoot());

        model.dialogProperty().onChanged(event -> Platform.runLater(() -> {
//            ItemModel oldDialog = event.getOldValue();
//            ItemModel newDialog = event.getNewValue();
//            if (newDialog instanceof CustomDialogModel) {
//                createCustomDialog((CustomDialogModel) newDialog);
//            } else if (newDialog instanceof SaveFileDialogModel) {
//                createSaveFileDialog((SaveFileDialogModel) newDialog);
//            } else if (newDialog instanceof InfoDialogModel) {
//                createInfoDialog((InfoDialogModel) newDialog);
//            } else if (newDialog instanceof ConfirmationDialogModel) {
//                createConfirmationDialog((ConfirmationDialogModel) newDialog);
//            } else if (newDialog instanceof UnexpectedErrorDialogModel) {
//                createUnexpectedErrorDialog((UnexpectedErrorDialogModel) newDialog);
//            } else if (newDialog instanceof QualifiedErrorDialogModel) {
//                createQualifiedErrorDialog((QualifiedErrorDialogModel) newDialog);
//            } else if (newDialog instanceof ShutdownDialogModel
//                    && !(oldDialog instanceof ShutdownDialogModel)) {
//                // Die if-Abfrage verhindert endlose Stapel mit ShutDown-Dialogen!
//                createShutdownDialog((ShutdownDialogModel) newDialog);
//            }
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
