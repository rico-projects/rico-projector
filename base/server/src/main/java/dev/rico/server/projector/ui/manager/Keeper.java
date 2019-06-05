package dev.rico.server.projector.ui.manager;

import dev.rico.common.projector.ui.IdentifiableModel;

import java.util.Objects;

class Keeper<T extends IdentifiableModel> implements Retained<T> {
    private final ManagedUiController managedUiController;
    private final T keepThis;

    Keeper(ManagedUiController managedUiController, T keepThis) {
        this.keepThis = Objects.requireNonNull(keepThis);
        this.managedUiController = Objects.requireNonNull(managedUiController);
//        managedUiController.getModel().getRetainedModels().add(keepThis);
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
