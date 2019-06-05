package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
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
