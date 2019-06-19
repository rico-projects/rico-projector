package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.client.projector.uimanager.QualifiedErrorDialog;
import dev.rico.internal.projector.ui.dialog.QualifiedErrorDialogModel;

import static dev.rico.client.remoting.FXBinder.bind;

public class QualifiedErrorDialogHandler implements ProjectorDialogHandler<QualifiedErrorDialogModel>, DialogConfiguration  {

    @Override
    public void show(final Projector projector, final QualifiedErrorDialogModel model) {
        QualifiedErrorDialog dialog = new QualifiedErrorDialog();
        configureDialog(projector, dialog, model);
        bind(dialog.errorTextProperty()).to(model.exceptionTextProperty());
        bind(dialog.headerTextProperty()).to(model.headerTextProperty());
        dialog.setContentText(model.getContentText());
        dialog.setRootCauseText(model.getRootCauseText());
        dialog.showAndWait();
    }

    @Override
    public Class<QualifiedErrorDialogModel> getSupportedType() {
        return QualifiedErrorDialogModel.class;
    }
}
