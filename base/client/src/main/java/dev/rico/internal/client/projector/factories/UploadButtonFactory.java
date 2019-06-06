package dev.rico.internal.client.projector.factories;

import dev.rico.client.Client;
import dev.rico.client.projector.Projector;
import dev.rico.core.http.HttpClient;
import dev.rico.internal.client.projector.uimanager.UnexpectedErrorDialog;
import dev.rico.internal.core.Assert;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.stage.FileChooser;
import to.remove.DocumentData;
import to.remove.ui.UploadButtonModel;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.stream.Collectors;

public class UploadButtonFactory extends ButtonBaseFactory<UploadButtonModel, ButtonBase> {
    @Override
    public ButtonBase create(Projector projector, UploadButtonModel itemModel) {
        ButtonBase button = createButtonBase(projector, itemModel, new Button());
        button.setOnAction(event -> onDoUpload(projector, event, itemModel));
        return button;
    }

    @Override
    public Class<UploadButtonModel> getSupportedType() {
        return null;
    }

    private void onDoUpload(Projector projector, ActionEvent actionEvent, UploadButtonModel uploadButton) {
        Assert.requireNonNull(uploadButton.getUploadUrl(), "buttonModel.uploadUrl");
        Button sourceButton = (Button) actionEvent.getSource();
        FileChooser chooser = new FileChooser();
        File loadFile = chooser.showOpenDialog(sourceButton.getScene().getWindow());
        if (loadFile != null && loadFile.exists() && loadFile.isFile() && !loadFile.isDirectory()) {
            try {
                DocumentData documentData = DocumentData.from(loadFile);
                Object detectedMimeType = documentData.getMimeType();
                if (detectedMimeType == null || uploadButton.getAllowedMimeTypes().stream()
                        .noneMatch(mimeType -> mimeType.equals(detectedMimeType.toString()))) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Das Dokument kann nicht verarbeitet werden.");
                    String allowedOnes = uploadButton.getAllowedMimeTypes().stream().collect(Collectors.joining("\n- ", "- ", ""));
                    if ("- ".equals(allowedOnes)) {
                        allowedOnes = "- (keine)";
                    }
                    alert.setContentText("Die Dokumentenverwaltung unterstützt die folgenden Dokument-Formate:\n\n" + allowedOnes
                            + "\n\nDie ausgewählte Datei wurde als '" + detectedMimeType + "' erkannt.");
                    alert.showAndWait();
                } else {
                    if (uploadButton.getOnUploadBeginAction() != null) {
                        doRemoteCall(projector, uploadButton.getOnUploadBeginAction());
                    }
                    sourceButton.setDisable(true);
                    HttpClient httpClient = Client.getService(HttpClient.class);
                    httpClient.put(uploadButton.getUploadUrl())
                            .withContent(documentData.getContent())
                            .readString().onError(e -> {
                        sourceButton.setDisable(false);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Das Dokument wurde nicht gespeichert.");
                        alert.setContentText("Die Dokumentenverwaltung konnte das Dokument nicht im System ablegen.");
                        alert.showAndWait();
                        if (uploadButton.getOnUploadFailedAction() != null) {
                            doRemoteCall(projector, uploadButton.getOnUploadFailedAction());
                        }
                    }).onDone(stringHttpResponse -> {
                        uploadButton.setStoreId(stringHttpResponse.getContent());
                        sourceButton.setDisable(false);
                        if (uploadButton.getOnUploadFinishedAction() != null) {
                            doRemoteCall(projector, uploadButton.getOnUploadFinishedAction());
                        }
                    }).execute();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
                throw new IllegalArgumentException(exception);
            }
        }
    }

    private void doRemoteCall(Projector projector, String remoteAction) {
        Objects.requireNonNull(remoteAction);
        projector.getControllerProxy().invoke(remoteAction)
                .exceptionally(throwable -> UnexpectedErrorDialog.showError(projector.getRoot(), throwable));
    }
}
