package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.TextAreaModel;
import dev.rico.remoting.Property;

public class PropertySheetTextAreaItemModel extends PropertySheetTextItemModel {
    private Property<TextAreaModel> field;

    public TextAreaModel getField() {
        return field.get();
    }

    public void setField(TextAreaModel field) {
        this.field.set(field);
    }

    public Property<TextAreaModel> fieldProperty() {
        return field;
    }
}
