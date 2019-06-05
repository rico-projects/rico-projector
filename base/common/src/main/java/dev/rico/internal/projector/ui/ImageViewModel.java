package dev.rico.internal.projector.ui;


import dev.rico.remoting.Property;

public class ImageViewModel extends ItemModel {
    private Property<String> resourcePath;
    private Property<Double> fitWidth;
    private Property<Double> fitHeight;
    private Property<Boolean> preserveRatio;

    public String getResourcePath() {
        return resourcePath.get();
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath.set(resourcePath);
    }

    public Property<String> resourcePathProperty() {
        return resourcePath;
    }

    public Double getFitWidth() {
        return fitWidth.get();
    }

    public void setFitWidth(Double fitWidth) {
        this.fitWidth.set(fitWidth);
    }

    public Property<Double> fitWidthProperty() {
        return fitWidth;
    }

    public Double getFitHeight() {
        return fitHeight.get();
    }

    public void setFitHeight(Double fitHeight) {
        this.fitHeight.set(fitHeight);
    }

    public Property<Double> fitHeightProperty() {
        return fitHeight;
    }

    public Boolean getPreserveRatio() {
        return preserveRatio.get();
    }

    public void setPreserveRatio(Boolean preserveRatio) {
        this.preserveRatio.set(preserveRatio);
    }

    public Property<Boolean> preserveRatioProperty() {
        return preserveRatio;
    }
}
