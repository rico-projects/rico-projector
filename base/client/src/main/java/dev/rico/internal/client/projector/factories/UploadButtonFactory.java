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
        final ButtonBase button = createButtonBase(projector, itemModel, new Button());
        button.setOnAction(event -> onDoUpload(projector, event, itemModel));
        return button;
    }

    @Override
    public Class<UploadButtonModel> getSupportedType() {
        return UploadButtonModel.class;
    }

    private void onDoUpload(final Projector projector, final ActionEvent actionEvent, final UploadButtonModel uploadButton) {
        Assert.requireNonNull(uploadButton.getUploadUrl(), "buttonModel.uploadUrl");
        final Button sourceButton = (Button) actionEvent.getSource();
        final FileChooser chooser = new FileChooser();
        final File loadFile = chooser.showOpenDialog(sourceButton.getScene().getWindow());
        if (loadFile != null && loadFile.exists() && loadFile.isFile() && !loadFile.isDirectory()) {
            try {
                final DocumentData documentData = DocumentData.from(loadFile);
                final Object detectedMimeType = documentData.getMimeType();
                if (detectedMimeType == null || uploadButton.getAllowedMimeTypes().stream()
                        .noneMatch(mimeType -> mimeType.equals(detectedMimeType.toString()))) {
                    final Alert alert = new Alert(Alert.AlertType.INFORMATION);
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
                    final HttpClient httpClient = Client.getService(HttpClient.class);
                    httpClient.put(uploadButton.getUploadUrl())
                            .withContent(documentData.getContent())
                            .readString().onError(e -> {
                        sourceButton.setDisable(false);
                        final Alert alert = new Alert(Alert.AlertType.ERROR);
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
            } catch (final IOException exception) {
                exception.printStackTrace();
                throw new IllegalArgumentException(exception);
            }
        }
    }

    private void doRemoteCall(final Projector projector, final String remoteAction) {
        Objects.requireNonNull(remoteAction);
        projector.getControllerProxy().invoke(remoteAction)
                .exceptionally(throwable -> UnexpectedErrorDialog.showError(projector.getRoot(), throwable));
    }
}
