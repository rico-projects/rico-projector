package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.projector.mixed.DocumentData;
import dev.rico.internal.client.projector.mixed.component.PdfViewer;
import dev.rico.internal.projector.ui.DocumentViewModel;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;

import java.util.List;
import java.util.stream.Collectors;

class DocumentViewer extends BorderPane {

    private final DocumentViewModel itemModel;
    private final ClientUiManager clientUiManager;
    private PdfViewer pdfViewer;
    private ImageViewer imageViewer;

    DocumentViewer(ClientUiManager clientUiManager, DocumentViewModel itemModel) {
        this.clientUiManager = clientUiManager;
        this.itemModel = itemModel;
    }

    void openDocument(DocumentData documentData, String title) {
        Object detectedMimeType = documentData.getMimeType();
        if (detectedMimeType == null || "application/pdf".equals(detectedMimeType.toString())) {
            PdfViewer pdfViewer = buildPdfViewer(documentData, title);
            setCenter(pdfViewer);
        } else {
            setCenter(buildImageViewer(documentData));
        }
    }

    private PdfViewer buildPdfViewer(DocumentData documentData, String title) {
        if (pdfViewer == null)
            pdfViewer = new PdfViewer();
        setTop(pdfViewer.getToolBar());
        pdfViewer.openDocument(documentData, title);
        addAdditionalToolBarItems(pdfViewer.getToolBar());
        return pdfViewer;
    }

    private Node buildImageViewer(DocumentData documentData) {
        if (imageViewer == null)
            imageViewer = new ImageViewer();
        imageViewer.setImage(new Image(documentData.asInputStream()));
        setTop(imageViewer.getToolBar());
        addAdditionalToolBarItems(imageViewer.getToolBar());
        return imageViewer;
    }

    private void addAdditionalToolBarItems(ToolBar toolBar) {
        if (!itemModel.getAdditionalToolBarItems().isEmpty()) {
            toolBar.getItems().add(new Separator());
        }
        List<Node> nodeList = itemModel.getAdditionalToolBarItems().stream()
                .map(clientUiManager::createNode)
                .filter(node -> !toolBar.getItems().contains(node))
                .collect(Collectors.toList());
        toolBar.getItems().addAll(nodeList);
    }

    void closeDocument() {
        if (getCenter() instanceof PdfViewer) {
            ((PdfViewer) getCenter()).closeDocument();
        } else if (getCenter() instanceof ImageViewer) {
            setCenter(null);
        }
    }
}
