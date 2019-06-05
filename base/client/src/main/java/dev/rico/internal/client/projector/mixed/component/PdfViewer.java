package dev.rico.internal.client.projector.mixed.component;

import dev.rico.internal.projector.mixed.DocumentData;
import com.github.rodionmoiseev.c10n.C10N;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import jfxtras.labs.scene.control.Magnifier;
import org.jpedal.examples.viewer.Commands;
import org.jpedal.examples.viewer.OpenViewerFX;
import org.jpedal.examples.viewer.gui.javafx.JavaFXThumbnailPanel;

public class PdfViewer extends BorderPane {
    private final ScrollPane scrollPane;
    private OpenViewerFX viewer = new OpenViewerFX(this, null);
    private ToolBar toolBar = new ToolBar();
    private static PdfViewerC10n C10 = C10N.get(PdfViewerC10n.class);

    public PdfViewer() {
        viewer.setupViewer();
        viewer.getSwingGUI().getMenuItems().dispose();
        JavaFXThumbnailPanel thumbnailPanel = (JavaFXThumbnailPanel) viewer.getSwingGUI().getThumbnailPanel();
        thumbnailPanel.setThumbnailsEnabled(false);

        SplitPane splitPane = (SplitPane) viewer.getRoot().getCenter();
        scrollPane = (ScrollPane) splitPane.getItems().get(1);
        scrollPane.setStyle(null);

        viewer.getRoot().setCenter(scrollPane);
        viewer.getRoot().setBottom(null);
        getChildren().clear();
        setCenter(scrollPane);

        // toolBar.getItems().add(createMagnifier());
        // toolBar.getItems().add(new Separator());
        toolBar.getItems().add(createResetZoom());
        toolBar.getItems().add(createZoomOut());
        toolBar.getItems().add(createZoomIn());
        toolBar.getItems().add(new Separator());
        toolBar.getItems().add(createNavigateBack());
        toolBar.getItems().add(createNavigateForward());
    }

    private Node createNavigateForward() {
        Button nextPageButton = new Button(">");
        nextPageButton.setOnAction(event -> navigatePageForward());
        return nextPageButton;
    }

    private Node createNavigateBack() {
        Button prevPageButton = new Button("<");
        prevPageButton.setOnAction(event -> navigatePageBack());
        return prevPageButton;
    }

    private Node createZoomOut() {
        Button zoomOutButton = new Button("-");
        zoomOutButton.setOnAction(event -> zoomOut());
        return zoomOutButton;
    }

    private Node createZoomIn() {
        Button zoomInButton = new Button("+");
        zoomInButton.setOnAction(event -> zoomIn());
        return zoomInButton;
    }

    private Button createResetZoom() {
        Button zoomOffButton = new Button("100%");
        zoomOffButton.setOnAction(event -> resetZoom());
        return zoomOffButton;
    }

    private Node createMagnifier() {
        ToggleButton toggleButton = new ToggleButton(C10.magnify());
        PdfViewer scrollPaneParent = (PdfViewer) scrollPane.getParent();
        Magnifier magnifier = new Magnifier(scrollPane);
        magnifier.activeProperty().bind(toggleButton.selectedProperty());
        magnifier.setRadius(150d);
        magnifier.setScaleFactor(3);
        magnifier.setScalableOnScroll(false);
        magnifier.setResizableOnScroll(false);
        scrollPaneParent.setCenter(magnifier);
        return toggleButton;
    }

    public void closeDocument() {
        viewer.getPdfDecoder().closePdfFile();
    }

    public void openDocument(DocumentData documentData, String title) {
        closeDocument();
        if (documentData != null) {
            viewer.executeCommand(Commands.OPENFILE, new Object[]{documentData.getContent(), title});
        }
    }

    private void resetZoom() {
        viewer.executeCommand(Commands.SCALING, new Object[]{"100"});
    }

    private void zoomIn() {
        viewer.executeCommand(Commands.ZOOMIN, null);
        // BUGFIX: Die folgende Zeile sorgt dafür, dass das Dokument
        // nach dem Zoomen neu gezeichnet wird.
        viewer.executeCommand(Commands.SCALING, new Object[]{"0.4"});
    }

    private void zoomOut() {
        viewer.executeCommand(Commands.ZOOMOUT, null);
        // BUGFIX: Die folgende Zeile sorgt dafür, dass das Dokument
        // nach dem Zoomen neu gezeichnet wird.
        viewer.executeCommand(Commands.SCALING, new Object[]{"0.4"});
    }

    private void navigatePageBack() {
        viewer.executeCommand(Commands.BACKPAGE, new Object[]{"1"});
    }

    private void navigatePageForward() {
        viewer.executeCommand(Commands.FORWARDPAGE, new Object[]{"1"});
    }

    public void addToolBarItem(Node item) {
        toolBar.getItems().add(item);
    }

    public void addToolBarItem(int pos, Node item) {
        toolBar.getItems().add(pos, item);
    }

    public ToolBar getToolBar() {
        return toolBar;
    }
}
