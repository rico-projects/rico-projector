package dev.rico.internal.projector.ui.propertysheet;


import dev.rico.internal.projector.ui.IdentifiableModel;
import dev.rico.remoting.ObservableList;
import dev.rico.remoting.Property;
import dev.rico.remoting.RemotingBean;


@RemotingBean
public abstract class PropertySheetItemModel extends IdentifiableModel {
    private Property<String> caption;
    private Property<String> description;
    private Property<String> stringValue;
    private Property<Boolean> readOnly;
    private Property<Boolean> alwaysVisible;
    private Property<Boolean> hideOnEdit;
    private ObservableList<String> validationMessages;

    public Boolean getReadOnly() {
        return readOnly.get();
    }

    public void setReadOnly(Boolean readOnly) {
        this.readOnly.set(readOnly);
    }

    public Property<Boolean> readOnlyProperty() {
        return readOnly;
    }

    public Boolean getHideOnEdit() {
        return hideOnEdit.get();
    }

    public void setHideOnEdit(Boolean hideOnEdit) {
        this.hideOnEdit.set(hideOnEdit);
    }

    public Property<Boolean> hideOnEditProperty() {
        return hideOnEdit;
    }

    public ObservableList<String> getValidationMessages() {
        return validationMessages;
    }

    public String getCaption() {
        return caption.get();
    }

    public void setCaption(String caption) {
        this.caption.set(caption);
    }

    public Property<String> captionProperty() {
        return caption;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public Property<String> descriptionProperty() {
        return description;
    }

    public String getStringValue() {
        return stringValue.get();
    }

    public void setStringValue(String stringValue) {
        this.stringValue.set(stringValue);
    }

    public Property<String> stringValueProperty() {
        return stringValue;
    }

    public Boolean getAlwaysVisible() {
        return alwaysVisible.get();
    }

    public void setAlwaysVisible(Boolean alwaysVisible) {
        this.alwaysVisible.set(alwaysVisible);
    }

    public Property<Boolean> alwaysVisibleProperty() {
        return alwaysVisible;
    }
}
