package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.IdentifiableModel;
import dev.rico.common.projector.ui.ItemModel;
import dev.rico.common.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingContext;

public interface ManagedUiController {
    ItemModel buildUi();

    ServerUiManager ui();

    RemotingContext getSession();

    BeanManager getBeanManager();

    ManagedUiModel getModel();

    default <T extends IdentifiableModel> Retained<T> retain(T model) {
        return new Keeper<>(this, model);
    }
}
