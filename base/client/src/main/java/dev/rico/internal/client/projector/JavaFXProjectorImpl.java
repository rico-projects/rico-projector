package dev.rico.internal.client.projector;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.client.remoting.ControllerProxy;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
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


    public JavaFXProjectorImpl(final ControllerProxy<? extends ManagedUiModel> controllerProxy) {
        this.controllerProxy = controllerProxy;

        factories = new HashMap<>();
        ServiceLoader.load(ProjectorNodeFactory.class).iterator().forEachRemaining(f -> {
            final Class<? extends ItemModel> type = f.getSupportedType();
            if(factories.containsKey(type)) {
                throw new IllegalStateException("Modeltype " + type + " is already defined by factory");
            }
            factories.put(f.getSupportedType(), f);
        });

    }

    @Override
    public <N extends Node> N createNode(final ItemModel itemModel) {
        ProjectorNodeFactory factory = factories.get(itemModel.getClass());
        if(factory == null) {
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
}
