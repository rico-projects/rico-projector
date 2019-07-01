package dev.rico.internal.client.projector.uimanager;

import dev.rico.internal.core.Assert;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//TODO: Looks like a factory / presenter for this class is missing. Should we remove it?
public class ImageViewer extends ScrollPane {

    private final ImageView imageView = new ImageView();
    private final ToolBar toolBar = new ToolBar();
    private final DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0);

    public ImageViewer() {
        imageView.fitWidthProperty().bind(widthProperty().multiply(zoomProperty).subtract(30));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        setContent(imageView);
        toolBar.getItems().add(createResetZoom());
        toolBar.getItems().add(createZoomOut());
        toolBar.getItems().add(createZoomIn());
    }

    public void setImage(final Image image) {
        imageView.setImage(image);
    }

    public void zoom(final double zoomFactor, final Point2D pointOnImage) {
        Assert.requireNonNull(pointOnImage, "pointOnImage");
        final double currentX = pointOnImage.getX();
        final double currentY = pointOnImage.getY();

        final double currentDistanceFromCenterX = currentX - getBoundsInLocal().getWidth() / 2;
        final double currentDistanceFromCenterY = currentY - getBoundsInLocal().getHeight() / 2;

        final double addScaleX = currentDistanceFromCenterX * zoomFactor;
        final double addScaleY = currentDistanceFromCenterY * zoomFactor;

        final double translationX = addScaleX - currentDistanceFromCenterX;
        final double translationY = addScaleY - currentDistanceFromCenterY;

        setTranslateX(getTranslateX() - translationX * getScaleX());
        setTranslateY(getTranslateY() - translationY * getScaleY());

        setScaleX(getScaleX() * zoomFactor);
        setScaleY(getScaleY() * zoomFactor);
    }


    private Node createZoomOut() {
        final Button zoomOutButton = new Button("-");
        zoomOutButton.setOnAction(event -> zoomOut());
        return zoomOutButton;
    }

    private Node createZoomIn() {
        final Button zoomInButton = new Button("+");
        zoomInButton.setOnAction(event -> zoomIn());
        return zoomInButton;
    }

    private Button createResetZoom() {
        final Button zoomOffButton = new Button("100%");
        zoomOffButton.setOnAction(event -> resetZoom());
        return zoomOffButton;
    }

    private void resetZoom() {
    }

    private void zoomIn() {
        zoomProperty.set(zoomProperty.get() * 1.1);
    }

    private void zoomOut() {
        zoomProperty.set(zoomProperty.get() / 1.1);
    }

    public ToolBar getToolBar() {
        return toolBar;
    }
}