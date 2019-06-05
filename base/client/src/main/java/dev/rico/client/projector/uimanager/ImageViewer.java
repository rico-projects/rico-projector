package dev.rico.client.projector.uimanager;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageViewer extends ScrollPane {

    //    private double mousePosX;
//    private double mousePosY;
    private ImageView imageView = new ImageView();
    private ToolBar toolBar = new ToolBar();
    private DoubleProperty zoomProperty = new SimpleDoubleProperty(1.0);

    ImageViewer() {
        imageView.fitWidthProperty().bind(widthProperty().multiply(zoomProperty).subtract(30));
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
//        imageView.fitWidthProperty().bind(widthProperty().subtract(30));
//        imageView.fitHeightProperty().bind(heightProperty());
        setContent(imageView);

        zoomProperty.addListener(arg0 -> {
//            imageView.setFitWidth(zoomProperty.get());
//            imageView.setFitHeight(zoomProperty.get());
        });

//        addEventFilter(ScrollEvent.ANY, new EventHandler<ScrollEvent>() {
//            @Override
//            public void handle(ScrollEvent event) {
//                if (event.getDeltaY() > 0) {
//                    zoomProperty.set(zoomProperty.get() * 1.1);
//                } else if (event.getDeltaY() < 0) {
//                    zoomProperty.set(zoomProperty.get() / 1.1);
//                }
//            }
//        });

//        setOnMousePressed(e -> {
//            mousePosX = e.getSceneX();
//            mousePosY = e.getSceneY();
//        });
//
//        setOnZoom(e -> {
//            Point2D pointOnImage = null;
//            try {
//                pointOnImage = getLocalToSceneTransform().inverseTransform(e.getSceneX(), e.getSceneY());
//            } catch (NonInvertibleTransformException e1) {
//                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//            }
//            zoom(e.getZoomFactor(), pointOnImage);
//        });
//
//        setOnMouseDragged(e -> {
//            double mouseDeltaX = (e.getSceneX() - mousePosX);
//            double mouseDeltaY = (e.getSceneY() - mousePosY);
//            mousePosX = e.getSceneX();
//            mousePosY = e.getSceneY();
//
//            setTranslateX(getTranslateX() + mouseDeltaX);
//            setTranslateY(getTranslateY() + mouseDeltaY);
//        });


        toolBar.getItems().add(createResetZoom());
        toolBar.getItems().add(createZoomOut());
        toolBar.getItems().add(createZoomIn());
    }

    public void setImage(Image image) {
        imageView.setImage(image);
    }

    public void zoom(double zoomFactor, Point2D pointOnImage) {
        double currentX = pointOnImage.getX();
        double currentY = pointOnImage.getY();

        double currentDistanceFromCenterX = currentX - getBoundsInLocal().getWidth() / 2;
        double currentDistanceFromCenterY = currentY - getBoundsInLocal().getHeight() / 2;

        double addScaleX = currentDistanceFromCenterX * zoomFactor;
        double addScaleY = currentDistanceFromCenterY * zoomFactor;

        double translationX = addScaleX - currentDistanceFromCenterX;
        double translationY = addScaleY - currentDistanceFromCenterY;

        setTranslateX(getTranslateX() - translationX * getScaleX());
        setTranslateY(getTranslateY() - translationY * getScaleY());

        setScaleX(getScaleX() * zoomFactor);
        setScaleY(getScaleY() * zoomFactor);
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

    private void resetZoom() {
    }

    private void zoomIn() {
        zoomProperty.set(zoomProperty.get() * 1.1);
    }

    private void zoomOut() {
        zoomProperty.set(zoomProperty.get() / 1.1);
    }

    ToolBar getToolBar() {
        return toolBar;
    }
}