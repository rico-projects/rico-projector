package dev.rico.internal.client.projector.dialoghandler;

import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorDialogHandler;
import dev.rico.internal.projector.ui.dialog.ShutdownDialogModel;
import dev.rico.internal.projector.ui.dialog.UnexpectedErrorDialogModel;
import javafx.scene.control.Alert;

public class ShutdownDialogHandler implements ProjectorDialogHandler<ShutdownDialogModel>, DialogConfiguration  {

    @Override
    public void show(final Projector projector, final ShutdownDialogModel model) {
        projector.getRoot().getScene().getWindow().hide();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        if (model.getOwner() != null) {
            alert.initOwner(projector.getModelToNodeMap().get(model.getOwner()).getScene().getWindow());
        }
        alert.setTitle("Neustart der Software");
        alert.setHeaderText("sprouts fly office erhält gerade eine Aktualisierung.");
        alert.setContentText("Die Anwendung hat sich deshalb automatisch beendet.");
        alert.showAndWait();
        System.exit(0);
    }

    @Override
    public Class<ShutdownDialogModel> getSupportedType() {
        return ShutdownDialogModel.class;
    }
}
