package dev.rico.internal.server.projector;

import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import to.remove.RemotingEvent;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class BaseServerUiManager {

    private final ManagedUiModel model;
    private final BeanManager beanManager;
    private final WeakHashMap<String, IdentifiableModel> idToItemMap = new WeakHashMap<>();
    private final WeakHashMap<IdentifiableModel, Runnable> modelToEventHandlerMap = new WeakHashMap<>();

    public BaseServerUiManager(ManagedUiModel model, BeanManager beanManager) {
        this.model = model;
        this.beanManager = beanManager;
    }

    public final <T extends IdentifiableModel> T create(Class<T> beanClass) {
        return create(beanClass, UUID.randomUUID().toString());
    }

    public final <T extends ItemModel> T getNodeById(String id) {
        return java.util.Objects.requireNonNull((T) idToItemMap.get(id), "Missing (injected?) node with id: " + id);
    }

    // TODO: Sollte irgendwie anders gehen?
    public final void removeId(String key) {
        idToItemMap.remove(key);
    }

    @Deprecated
    public final <T extends RemotingEvent> T createEvent(Class<T> eventClass) {
        return beanManager.create(eventClass);
    }

    @Deprecated
    public final <T extends RemotingEvent> void sendEvent(T event) {
        model.setEvent(event);
    }

    // TODO: Auf neues Action-System umstellen
    @Deprecated
    protected final void installActionHandler(IdentifiableModel model, Runnable handler) {
        Assert.requireNonNull(model, "model");
        Assert.requireNonNull(handler, "handler");
        modelToEventHandlerMap.put(model, handler);
    }

    protected final void receivedButtonClick(IdentifiableModel button) {
        if (modelToEventHandlerMap.containsKey(button)) {
            modelToEventHandlerMap.get(button).run();
        }
    }

    private final <T extends IdentifiableModel> T create(Class<T> beanClass, String id) {
        T model = beanManager.create(beanClass);
        model.setId(id);
        if (idToItemMap.containsKey(id)) {
            throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + id + "' is already registered");
        }
        idToItemMap.put(id, model);
        model.idProperty().onChanged(evt -> {
            if (idToItemMap.containsKey(evt.getNewValue())) {
                throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + evt.getNewValue() + "' is already registered");
            }
            String oldId = findInMap(idToItemMap, model);
            idToItemMap.put(evt.getNewValue(), model);
            idToItemMap.remove(oldId);
        });
        if (model instanceof ItemModel) {
            ItemModel itemModel = (ItemModel) model;
            itemModel.setDisable(false);
            itemModel.setManaged(true);
            itemModel.setVisible(true);
        }
        return model;
    }

    private final <T> String findInMap(Map<String, T> map, T model) {
        java.util.Objects.requireNonNull(map);
        java.util.Objects.requireNonNull(model);
        for (Map.Entry<String, T> entry : map.entrySet()) {
            if (entry.getValue() == model) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Could not find '" + model + "' in map");
    }

}
