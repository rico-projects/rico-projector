package dev.rico.internal.projector.ui;

import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;
import to.remove.ui.MessagePlaceholder;

@RemotingBean
public class ItemModel extends IdentifiableModel {
    private Property<Boolean> disable;
    private Property<Boolean> visible;
    private Property<Boolean> managed;
    private Property<Double> maxWidth;
    private Property<Double> maxHeight;
    private Property<Double> prefWidth;
    private Property<Double> prefHeight;
    private Property<String> style;
    private Property<ItemModel> messageDisplay;
    private ObservableList<String> styleClass;
    private ObservableList<String> validationMessages;

    public Double getPrefWidth() {
        return prefWidth.get();
    }

    public void setPrefWidth(Double prefWidth) {
        this.prefWidth.set(prefWidth);
    }

    public Property<Double> prefWidthProperty() {
        return prefWidth;
    }

    public Double getPrefHeight() {
        return prefHeight.get();
    }

    public void setPrefHeight(Double prefHeight) {
        this.prefHeight.set(prefHeight);
    }

    public Property<Double> prefHeightProperty() {
        return prefHeight;
    }

    public Boolean getVisible() {
        return visible.get();
    }

    public void setVisible(Boolean visible) {
        this.visible.set(visible);
    }

    public Property<Boolean> visibleProperty() {
        return visible;
    }

    public Boolean getManaged() {
        return managed.get();
    }

    public void setManaged(Boolean managed) {
        this.managed.set(managed);
    }

    public Property<Boolean> managedProperty() {
        return managed;
    }

    public String getStyle() {
        return style.get();
    }

    public void setStyle(String style) {
        this.style.set(style);
    }

    public Property<String> styleProperty() {
        return style;
    }

    public ObservableList<String> getStyleClass() {
        return styleClass;
    }

    public ObservableList<String> getValidationMessages() {
        return validationMessages;
    }

    public ItemModel getMessageDisplay() {
        return messageDisplay.get();
    }

    public void setMessageDisplay(MessagePlaceholder messageDisplay) {
        this.messageDisplay.set(messageDisplay);
    }

    public Property<ItemModel> messageDisplayProperty() {
        return messageDisplay;
    }

    public Boolean getDisable() {
        return disable.get();
    }

    public void setDisable(Boolean disable) {
        this.disable.set(disable);
    }

    public Property<Boolean> disableProperty() {
        return disable;
    }

    public Double getMaxWidth() {
        return maxWidth.get();
    }

    public void setMaxWidth(Double maxWidth) {
        this.maxWidth.set(maxWidth);
    }

    public Property<Double> maxWidthProperty() {
        return maxWidth;
    }

    public Double getMaxHeight() {
        return maxHeight.get();
    }

    public void setMaxHeight(Double maxHeight) {
        this.maxHeight.set(maxHeight);
    }

    public Property<Double> maxHeightProperty() {
        return maxHeight;
    }
}
