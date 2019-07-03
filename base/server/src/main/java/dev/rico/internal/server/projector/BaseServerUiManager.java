package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.remoting.BeanManager;

import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

public class BaseServerUiManager {

    private final BeanManager beanManager;
    private final WeakHashMap<String, IdentifiableModel> idToItemMap = new WeakHashMap<>();

    public BaseServerUiManager(final BeanManager beanManager) {
        this.beanManager = beanManager;
    }

    public final <T extends IdentifiableModel> T create(final Class<T> beanClass) {
        return create(beanClass, UUID.randomUUID().toString());
    }

    public final <T extends ItemModel> T getNodeById(final String id) {
        return java.util.Objects.requireNonNull((T) idToItemMap.get(id), "Missing (injected?) node with id: " + id);
    }

    private final <T extends IdentifiableModel> T create(final Class<T> beanClass, final String id) {
        final T model = beanManager.create(beanClass);
        model.setId(id);
        if (idToItemMap.containsKey(id)) {
            throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + id + "' is already registered");
        }
        idToItemMap.put(id, model);
        model.idProperty().onChanged(evt -> {
            if (idToItemMap.containsKey(evt.getNewValue())) {
                throw new IllegalArgumentException("Object of class '" + beanClass.getName() + "' with id '" + evt.getNewValue() + "' is already registered");
            }
            final String oldId = findInMap(idToItemMap, model);
            idToItemMap.put(evt.getNewValue(), model);
            idToItemMap.remove(oldId);
        });
        if (model instanceof ItemModel) {
            final ItemModel itemModel = (ItemModel) model;
            itemModel.setDisable(false);
            itemModel.setManaged(true);
            itemModel.setVisible(true);
        }
        return model;
    }

    private final <T> String findInMap(final Map<String, T> map, final T model) {
        java.util.Objects.requireNonNull(map);
        java.util.Objects.requireNonNull(model);
        for (final Map.Entry<String, T> entry : map.entrySet()) {
            if (entry.getValue() == model) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Could not find '" + model + "' in map");
    }

}
