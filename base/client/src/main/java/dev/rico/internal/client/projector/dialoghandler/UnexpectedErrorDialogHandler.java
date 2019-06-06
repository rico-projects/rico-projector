package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.projector.ui.dialog.UnexpectedErrorDialogModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class UnexpectedErrorDialogHandler implements ProjectorDialogHandler<UnexpectedErrorDialogModel>, DialogConfiguration {

    @Override
    public void show(final Projector projector, final UnexpectedErrorDialogModel model) {
        final UnexpectedErrorDialog dialog = new UnexpectedErrorDialog();
        configureDialog(projector, dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        dialog.showAndWait();
    }

    @Override
    public Class<UnexpectedErrorDialogModel> getSupportedType() {
        return UnexpectedErrorDialogModel.class;
    }
}
