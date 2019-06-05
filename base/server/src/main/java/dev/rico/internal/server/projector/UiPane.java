package dev.rico.internal.server.projector;

import dev.rico.internal.projector.ui.ItemModel;
import dev.rico.internal.projector.ui.ManagedUiModel;
import dev.rico.remoting.BeanManager;
import dev.rico.server.remoting.RemotingContext;

public abstract class UiPane implements ManagedUiController {
    private final ManagedUiController delegate;
    private Retained<ItemModel> pane;

    public UiPane(ManagedUiController delegate) {
        this.delegate = delegate;
    }

    protected ManagedUiController getDelegate() {
        return delegate;
    }

    public ServerUiManager ui() {
        return delegate.ui();
    }

    public RemotingContext getSession() {
        return delegate.getSession();
    }

    @Override
    public BeanManager getBeanManager() {
        return delegate.getBeanManager();
    }

    public ManagedUiModel getModel() {
        return delegate.getModel();
    }

    public boolean isCreated() {
        return pane != null;
    }

    public ItemModel getPane() {
        if (pane == null) {
            pane = retain(buildUi());
        }
        return pane.get();
    }
}
