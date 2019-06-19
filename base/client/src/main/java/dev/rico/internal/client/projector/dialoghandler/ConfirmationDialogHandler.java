package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.projector.ui.dialog.ConfirmationDialogModel;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Objects;
import java.util.Optional;

public class ConfirmationDialogHandler implements ProjectorDialogHandler<ConfirmationDialogModel>, DialogConfiguration {

    @Override
    public void show(final Projector projector, final ConfirmationDialogModel model) {
        Objects.requireNonNull(model.getOkayAction());
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Optional<Node> nodeOptional = configureDialog(projector, alert, model);
        alert.setHeaderText(model.getHeaderText());
        alert.setContentText(model.getContentText());
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                projector.getControllerProxy().invoke(model.getOkayAction()).exceptionally(throwable -> UnexpectedErrorDialog.showError(nodeOptional.orElse(null), throwable));
            }
        });
    }

    @Override
    public Class<ConfirmationDialogModel> getSupportedType() {
        return ConfirmationDialogModel.class;
    }
}
