package to.remove.uimanager;

import dev.rico.client.projector.Projector;
import dev.rico.internal.client.projector.uimanager.ImageViewer;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import to.remove.DocumentData;
import to.remove.PdfViewer;
import to.remove.ui.DocumentViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class DocumentViewer extends BorderPane {

    private final Projector projector;
    private final DocumentViewModel itemModel;
    private PdfViewer pdfViewer;
    private ImageViewer imageViewer;

    public DocumentViewer(Projector projector, DocumentViewModel itemModel) {
        this.projector = projector;
        this.itemModel = itemModel;
    }

    public void openDocument(DocumentData documentData, String title) {
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
                .map(itemModel1 -> (Node) projector.createNode(itemModel1))
                .filter(node -> !toolBar.getItems().contains(node))
                .collect(Collectors.toList());
        toolBar.getItems().addAll(nodeList);
    }

    public void closeDocument() {
        if (getCenter() instanceof PdfViewer) {
            ((PdfViewer) getCenter()).closeDocument();
        } else if (getCenter() instanceof ImageViewer) {
            setCenter(null);
        }
    }
}
