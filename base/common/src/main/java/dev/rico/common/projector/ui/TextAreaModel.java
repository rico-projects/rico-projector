package dev.rico.common.projector.ui;


import dev.rico.remoting.Property;

public class TextAreaModel extends TextFieldModel {
    private Property<Boolean> wrapText;
    private Property<Integer> prefRowCount;

    public Boolean getWrapText() {
        return wrapText.get();
    }

    public void setWrapText(Boolean wrapText) {
        this.wrapText.set(wrapText);
    }

    public Property<Boolean> wrapTextProperty() {
        return wrapText;
    }

    public Integer getPrefRowCount() {
        return prefRowCount.get();
    }

    public void setPrefRowCount(Integer prefRowCount) {
        this.prefRowCount.set(prefRowCount);
    }

    public Property<Integer> prefRowCountProperty() {
        return prefRowCount;
    }
}
