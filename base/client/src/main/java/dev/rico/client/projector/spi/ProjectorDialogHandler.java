package dev.rico.client.projector.spi;

import dev.rico.client.projector.Projector;
import dev.rico.internal.projector.ui.dialog.DialogModel;

public interface ProjectorDialogHandler<M extends DialogModel> extends TypeBasedProvider<M> {

    /**
     * Results from dialogs are handled in model (properties) or by action calls on controller.
     *
     * @param model
     */
    void show(Projector projector, M model);
}
