package dev.rico.common.projector.ui.propertysheet;


import dev.rico.common.projector.ui.CheckBoxModel;
import dev.rico.remoting.Property;

public class PropertySheetCheckBoxItemModel extends PropertySheetItemModel {
    private Property<CheckBoxModel> field;

    public CheckBoxModel getField() {
        return field.get();
    }

    public void setField(CheckBoxModel field) {
        this.field.set(field);
    }

    public Property<CheckBoxModel> fieldProperty() {
        return field;
    }
}
