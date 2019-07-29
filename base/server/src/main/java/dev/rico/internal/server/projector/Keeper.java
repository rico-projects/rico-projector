package dev.rico.internal.server.projector;

import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.ui.IdentifiableModel;

class Keeper<T extends IdentifiableModel> implements Retained<T> {
    private final ManagedUiController managedUiController;
    private final T keepThis;

    Keeper(final ManagedUiController managedUiController, final T keepThis) {
        this.keepThis = Assert.requireNonNull(keepThis, "keepThis");
        this.managedUiController = Assert.requireNonNull(managedUiController, "managedUiController");
    }

    Keeper() {
        this.managedUiController = null;
        this.keepThis = null;
    }

    public T get() {
        return keepThis;
    }

    @Override
    protected void finalize() {
        if (managedUiController != null && keepThis != null) {
            managedUiController.getSession().createSessionExecutor().runLaterInClientSession(
                    () -> managedUiController.getModel().getRetainedModels().remove(keepThis));
        }
    }
}
