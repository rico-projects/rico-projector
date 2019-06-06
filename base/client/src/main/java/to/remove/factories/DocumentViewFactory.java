package to.remove.factories;

import dev.rico.client.Client;
import dev.rico.client.projector.Projector;
import dev.rico.client.projector.spi.ProjectorNodeFactory;
import dev.rico.core.http.HttpClient;
import dev.rico.core.http.RequestMethod;
import dev.rico.internal.client.projector.mixed.Configuration;
import dev.rico.internal.client.projector.uimanager.AsyncSequence;
import dev.rico.internal.core.Assert;
import dev.rico.internal.projector.mixed.CommonUiHelper;
import javafx.scene.control.Alert;
import to.remove.DocumentData;
import to.remove.ui.DocumentViewModel;
import to.remove.uimanager.DocumentViewer;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class DocumentViewFactory implements ProjectorNodeFactory<DocumentViewModel, DocumentViewer> {

    @Override
    public DocumentViewer create(final Projector projector, final DocumentViewModel model) {
        Assert.requireNonNull(projector, "projector");
        Assert.requireNonNull(model, "model");

        DocumentViewer documentViewer = new DocumentViewer(projector, model);
        Consumer<DocumentData> consumer = documentData -> {
            documentViewer.closeDocument();
            if (documentData != null) {
                documentViewer.openDocument(documentData, "");
            }
        };
        consumer.accept(model.getDocumentData());
        CommonUiHelper.subscribeWithOptional(model.documentByIdProperty(), idOptional -> {
            documentViewer.closeDocument();
            idOptional.ifPresent(id -> loadDocumentFromServerAndShow(id, consumer));
        });
        model.documentDataProperty().onChanged(evt -> consumer.accept(evt.getNewValue()));
        return documentViewer;
    }

    protected void loadDocumentFromServerAndShow(String documentId, Consumer<DocumentData> onFinish) {
        AsyncSequence.doAsync(() -> {
            try {
                if (documentId == null) {
                    return null;
                }
                HttpClient httpClient = Client.getService(HttpClient.class);
                return DocumentData.from(httpClient.request(Configuration.getServerUrl() + "/api/document/get/" + documentId, RequestMethod.GET)
                        .withoutContent().readBytes()
                        .execute().get().getContent().get());
            } catch (InterruptedException | ExecutionException e) {
                return null;
            }
        }, onFinish, exception -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Dokument nicht verfügbar.");
            alert.setContentText("Das System konnte das Dokument nicht abrufen.");
            alert.showAndWait();
        });
    }

    @Override
    public Class<DocumentViewModel> getSupportedType() {
        return DocumentViewModel.class;
    }
}
