package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.projector.ui.dialog.InfoDialogModel;
import javafx.scene.control.Alert;

public class InfoDialogHandler implements ProjectorDialogHandler<InfoDialogModel>, DialogConfiguration  {

    @Override
    public void show(final Projector projector, final InfoDialogModel model) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        configureDialog(projector, alert, model);
        alert.setHeaderText(model.getHeaderText());
        alert.setContentText(model.getContentText());
        alert.showAndWait();
    }

    @Override
    public Class<InfoDialogModel> getSupportedType() {
        return InfoDialogModel.class;
    }
}
